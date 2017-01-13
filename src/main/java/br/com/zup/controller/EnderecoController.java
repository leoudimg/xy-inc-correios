package br.com.zup.controller;

import br.com.zup.model.Endereco;
import br.com.zup.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * Classe que contem as regras de negocio referente ao Modelo Endereco
 * 
 * 
 * 
 * @author Leonardo Henrique Lages Pereira - 11/01/2017
 *
 */
public class EnderecoController {

  private static final Logger logger = Logger.getLogger(EnderecoController.class.getName());

  /*
   * Chave que contem a url do servico de busca por CEP dos correios.
   */
  private static final String CHAVE_BUSCA_POR_CEP = "busca.por.cep.url";

  /*
   * Chave que contem a url do servico de busca por Logradouro dos correios.
   */
  private static final String CHAVE_BUSCA_POR_LOGRADOURO = "busca.por.logradouro.url";

  /*
   * Chave referente ao Metodo http a ser utilizado no servico busca por cep
   */
  private static final String CHAVE_HTTP_METHOD = "busca.por.cep.http.method";

  /*
   * Chave referente ao Metodo http a ser utilizado no servico busca por
   * logradouro
   */
  private static final String CHAVE_HTTP_METHOD_LOGRADOURO = "busca.por.logradouro.http.method";

  /*
   * Chave referente ao nome da classe da tabela a ser pesquisada no html de
   * retorno
   */
  private static final String CHAVE_NOME_CLASS_TABELA = "busca.por.cep.nome.class.tabela";

  /*
   * CEP
   */
  private static final String PARAMETRO_CEP = "CEP";

  /*
   * CEP
   */
  private static final String PARAMETRO_RELAXATION = "relaxation";

  /*
   * Parametro referente ao combo
   */
  private static final String PARAMETRO_TIPO_CEP = "tipoCEP";

  /*
   * Atributo necessario para a pesquisa
   */
  private static final String PARAMETRO_SEMELHANTE = "semelhante";

  /*
   * Filtro para pesquisar todos os tipos
   */
  private static final String FILTRO_TODOS = "ALL";

  /*
   * Letra N
   */
  private static final String NAO_SIGLA = "N";

  /*
   * Metodo POST
   */
  private static final String METODO_POST = "POST";

  /*
   * Tag html referente a linha da tabela
   */
  private static final String TAG_TR = "tr";

  /*
   * Tag html referente a coluna da linha
   */
  private static final String TAG_TD = "td";

  /**
   * Realiza a busca de enderecos com base no CEP informado utilizando Jsoup
   * connect.
   * 
   * @param cep
   *          - CEP informado pelo sistema de origem
   * @return - Lista de enderecos referente ao CEP.
   */
  public List<Endereco> buscarEnderecoPorCep(String cep) {

    if (StringUtils.isNotBlank(cep)) {

      final String url = PropsUtil.getPropriedade(CHAVE_BUSCA_POR_CEP);

      if (StringUtils.isNotBlank(url)) {

        try {

          Document doc;

          if (METODO_POST.equals(PropsUtil.getPropriedade(CHAVE_HTTP_METHOD_LOGRADOURO))) {

            doc = Jsoup.connect(url).data(PARAMETRO_CEP, cep).post();

          } else {

            doc = Jsoup.connect(url).data(PARAMETRO_CEP, cep).get();

          }

          return parsearHtml(doc);

        } catch (IOException erro) {
          logger.error("Ocorreu um erro ao submeter a requisicao.", erro);
        }
      }

    }

    return null;
  }

  /**
   * Realiza a busca de enderecos com base no CEP informado utilizando
   * HTTPUrlConnection.
   * 
   * @param logradouro
   *          - Logradouro informado pelo sistema de origem
   * @return - Lista de enderecos referente ao CEP.
   */
  public String buscarEnderecoLogradouro(String logradouro) {

    if (StringUtils.isNotBlank(logradouro)) {

      final String url = PropsUtil.getPropriedade(CHAVE_BUSCA_POR_LOGRADOURO);

      if (StringUtils.isNotBlank(url)) {

        URL obj = null;
        try {
          final URI uri = new URI(url);

          obj = uri.toURL();
        } catch (Exception erro) {
          logger.error("Ocorreu um erro URL Incorreta: " + url, erro);
        }

        if (obj != null) {

          HttpURLConnection con = prepararConexao(obj);

          try {

            Map<String, String> parametros = new HashMap<String, String>();

            parametros.put(PARAMETRO_SEMELHANTE, NAO_SIGLA);
            parametros.put(PARAMETRO_TIPO_CEP, FILTRO_TODOS);
            parametros.put(PARAMETRO_RELAXATION, logradouro);

            prepararParametros(parametros, con);

            int responseCode = con.getResponseCode();

            String html = "";

            if (responseCode == 200) {

              String tmp = "";

              BufferedReader in = new BufferedReader(
                  new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8), 10000);

              StringBuilder builder = new StringBuilder();

              while ((tmp = in.readLine()) != null) {
                builder.append(tmp);
              }

              in.close();
              con.disconnect();

              html = builder.toString();
            }

            if (StringUtils.isNotBlank(html)) {

              Document doc = Jsoup.parse(html);

              List<Endereco> enderecos = parsearHtml(doc);

              if (CollectionUtils.isNotEmpty(enderecos)) {
                return enderecos.get(0).getCep();
              }

            }

          } catch (IOException erro) {
            // TODO Auto-generated catch block
            erro.printStackTrace();
          }
        }
      }
    }

    return null;

  }

  /**
   * Realiza o parser do html obtido atraves da submissao dos parametros.
   * 
   * @param doc
   *          HTML resultante
   * @return Lista de Enderecos
   */
  private List<Endereco> parsearHtml(Document doc) {

    List<Endereco> enderecos = new ArrayList<Endereco>();

    Element tabela = doc.select("." + PropsUtil.getPropriedade(CHAVE_NOME_CLASS_TABELA)).first();

    if (tabela != null) {

      Elements trs = tabela.getElementsByTag(TAG_TR);

      Iterator<Element> linhas = trs.iterator();

      if (linhas.hasNext()) {

        linhas.next();// Ignora o cabecalho

        while (linhas.hasNext()) {
          Element linha = linhas.next();

          Endereco end = new Endereco();

          Elements tds = linha.getElementsByTag(TAG_TD);

          end.setLogradouro(tds.get(0).html().replace("&nbsp;", ""));
          end.setBairro(tds.get(1).html().replace("&nbsp;", ""));
          end.setUf(tds.get(2).html());
          end.setCep(tds.get(3).html());

          enderecos.add(end);
        }

      }
    }

    return enderecos;
  }

  /**
   * Prepara a conexao http dado a URL de destino.
   * 
   * @param obj
   *          Url de destino
   * @return HttpURLConnection
   */
  private HttpURLConnection prepararConexao(URL obj) {
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod(PropsUtil.getPropriedade(CHAVE_HTTP_METHOD));
      con.setDoOutput(Boolean.TRUE);
      con.setConnectTimeout(0);
      con.setReadTimeout(0);
      con.setUseCaches(false);
      con.setDoInput(true);
    } catch (IOException exception) {
      logger.error("Ocorreu um erro ao abrir a conexao.", exception);
    }

    return con;

  }

  /**
   * Prepara os parametros que serao enviados via post.
   * 
   * @param parametros
   *          Parametros a serem enviados.
   * @param con
   *          Conexao atraves da qual serao enviados os parametros.
   */
  private void prepararParametros(Map<String, String> parametros, HttpURLConnection con) {

    StringBuilder builder = new StringBuilder();

    try {

      for (Entry<String, String> parametro : parametros.entrySet()) {
        if (builder.length() != 0) {
          builder.append('&');
        }
        builder.append(URLEncoder.encode(parametro.getKey(), StandardCharsets.UTF_8.toString()));
        builder.append("=");
        builder.append(URLEncoder.encode(parametro.getValue(), StandardCharsets.UTF_8.toString()));

      }

    } catch (UnsupportedEncodingException exception) {
      logger.error("Ocorreu um erro ao realizar o encoding dos parametros.", exception);
    }

    OutputStream os;

    try {

      os = con.getOutputStream();
      os.write(builder.toString().getBytes());
      os.flush();
      os.close();

    } catch (IOException exception) {
      logger.error("Ocorreu um erro ao preparar os parametros.", exception);
    }
  }

}
