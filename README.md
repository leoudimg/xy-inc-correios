xy-inc-correios
==========================

### Descrição

Este aplicativo é uma API REST para consulta de CEP e logradouro diretamente no site dos correios.<br>
Para construir este aplicativo foi utilizado apenas o framework Jersey para expor os dois serviços especificados. Para fazer a integração com o site dos correios foi utilizado uma biblioteca chamada JSoup para realizar o parser do html resultante e também foi utilizada para realizar a conexão com a página.<br>
Além disso, foi introduzido o uso de HTTPUrlConnection em um dos serviço para demonstrar outra possibilidade de conexão.<br>
Foi utilizado o Servlet Container Jetty por ser simples, rápido e possuir um plugin para maven para que o mesmo seja iniciado automaticamente após o build.<br>
Também foi utilizado o plugin jacoco para poder gerar as métricas dos testes unitários e disponibilizar os resultados em uma página html.

### Instalação 

Basta baixar o código fonte, configurar o maven na máquina onde irá rodar o servidor e executar o comando mvn jetty:run no diretório raiz da aplicação.


### Rodando a suíte de testes

Para rodar os testes unitários basta utilizar o comando mvn verify, após a finalização deste, é criado um relatório em html na pasta target/site/jacoco.


### Consumindo os serviços

Para consumir os serviços basta utilizar via GET as seguintes URLS:<br>

http://localhost:8080/xy-inc-correios/rest/enderecoService/buscarEnderecoCEP/{CEP valido}<br>
http://localhost:8080/xy-inc-correios/rest/enderecoService/buscarCEPLogradouro/{logradouro}<br>

A tratativa de erros é realizada via códigos http. Um arquivo de log é criado ao executar a aplicação.

Foi criada uma tela simples para testar a API utilizando apenas html e jQuery, a mesma pode ser salva como html em outros dispositivos ajustando o hostname para realizar os acessos. Para acessar a página basta acessar a URL: http://localhost:8080/xy-inc-correios
