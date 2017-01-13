package br.com.zup.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe utilitaria para obtencao do arquivo de configuracao e suas
 * propriedades.
 * 
 * @author Leonardo Henrique Lages Pereira - 11/01/2017
 */
public class PropsUtil {

  private static Properties properties;

  private static final String RESOURCE_NAME = "config.properties";

  /*
   * Carrega o arquivo de configuracao
   */
  private static void initializeProperties() {
    properties = new Properties();
    InputStream input = PropsUtil.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);

    try {
      properties.load(input);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    if (input != null) {
      try {
        input.close();
      } catch (IOException erro) {
        erro.printStackTrace();
      }
    }

  }

  /**
   * Prove o arquivo de configuracao.
   * 
   * @return arquivo de configuracao
   */
  public static Properties getProperties() {
    if (properties == null) {
      initializeProperties();
    }

    return properties;
  }

  /*
   * Retorna o valor configurado para a chave informado no arquivo de
   * configuracao.
   * 
   * @param chave
   * 
   * @return
   */
  public static String getPropriedade(String chave) {
    return getProperties().getProperty(chave);
  }

}
