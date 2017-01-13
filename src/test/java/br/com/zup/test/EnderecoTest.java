package br.com.zup.test;

import br.com.zup.model.Endereco;
import br.com.zup.services.EnderecoService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Classe de testes referente ao modelo Endereco.
 * 
 * @author Leonardo Henrique Lages Pereira 12/01/2017
 *
 */
public class EnderecoTest {

  private EnderecoService enderecoService;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    enderecoService = new EnderecoService();
  }

  @Test
  public void buscarEnderecoLogradouroCep_Ok() {

    List<String> cep = enderecoService.buscarEnderecoLogradouro("Rua Iracema Oliveira Godoy");

    Assert.assertNotNull(cep);

  }

  @Test
  public void buscarEnderecoCep_Ok() {

    List<Endereco> listaEndereco = enderecoService.buscarEnderecoCep("38415-045");

    Assert.assertNotNull(listaEndereco);

  }

}
