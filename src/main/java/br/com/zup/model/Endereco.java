package br.com.zup.model;

/*
 * Modelo que representa um endereço
 * 
 * @author Leonardo Henrique Lages Pereira - 11/01/2017
 *
 */
public class Endereco {

  private String logradouro;
  private String bairro;
  private String uf;
  private String cep;

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

}
