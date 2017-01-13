package br.com.zup.services;

import br.com.zup.controller.EnderecoController;
import br.com.zup.model.Endereco;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 * Classe onde se encontra a implementacao dos serviços REST referente ao modelo
 * Endereco.
 * 
 * @author Leonardo Henrique Lages Pereira - 12/01/2017
 */
@Path("enderecoService")
public class EnderecoService {

  private static final Logger logger = Logger.getLogger(EnderecoService.class.getName());

  private EnderecoController controller;

  private static final String PADRAO_CEP = "\\d{5}-\\d{3}";
  private static final String ERROR_CEP_INVALIDO = "CEP inválido.";
  private static final String ERROR_REQUEST = "Ocorreu um erro ao processar a requisição.";
  private static final String ERROR_NOT_FOUND = "Não foram encontrados registros para a pesquisa.";
  private static final int CODE_ERROR_BAD_REQUEST = 400;
  private static final int CODE_ERROR_NOT_FOUND = 404;

  /**
   * Operacao que busca um Endereco dado o CEP.
   * 
   * @param cep
   *          CEP informado pelo sistema de origem
   * @return Endereco
   */
  @GET
  @Path("buscarEnderecoCEP/{CEP}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Endereco> buscarEnderecoCep(@PathParam("CEP") String cep) {

    controller = new EnderecoController();

    if (!cep.matches(PADRAO_CEP)) {

      throw new WebApplicationException(ERROR_CEP_INVALIDO, CODE_ERROR_BAD_REQUEST);
    }

    List<Endereco> retorno = controller.buscarEnderecoPorCep(cep);

    if (retorno == null) {

      logger.error(ERROR_REQUEST);

      throw new WebApplicationException(ERROR_REQUEST, CODE_ERROR_BAD_REQUEST);

    } else if (CollectionUtils.isEmpty(retorno)) {

      logger.error(ERROR_NOT_FOUND);

      throw new WebApplicationException(ERROR_NOT_FOUND, CODE_ERROR_NOT_FOUND);

    } else {

      return retorno;

    }
  }

  /**
   * Operacao que busca um CEP dado o logradouro.
   * 
   * @param logradouro
   *          logradouro a ser pesquisado
   * @return cep referente ao logradouro
   */
  @GET
  @Path("buscarCEPLogradouro/{logradouro}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> buscarEnderecoLogradouro(@PathParam("logradouro") String logradouro) {

    controller = new EnderecoController();

    List<String> retorno = controller.buscarEnderecoLogradouro(logradouro);

    if (retorno == null) {

      logger.error(ERROR_REQUEST);

      throw new WebApplicationException(ERROR_REQUEST, CODE_ERROR_BAD_REQUEST);

    } else if (CollectionUtils.isEmpty(retorno)) {

      logger.error(ERROR_NOT_FOUND);

      throw new WebApplicationException(ERROR_NOT_FOUND, CODE_ERROR_NOT_FOUND);

    } else {
      return retorno;
    }

  }

}
