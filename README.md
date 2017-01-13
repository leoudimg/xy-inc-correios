xy-inc-correios
==========================

### Descrição

Este aplicativo é uma API REST para consulta de CEP e logradouro diretamente
no site dos correios.
Para construir este aplicativo foi utilizado apenas o framework Jersey para 
expor os dois serviços especificados. Para fazer a integração com o site dos 
correios foi utilizado uma biblioteca chamada JSoup para realizar o parser do
html resultante e também foi utilizada para realizar a conexão com a página. Também 
foi utilizado HTTPUrlConnection em um dos serviço para demonstrar outra possibilidade 
de conexão.
Foi utilizado o Servlet Container Jetty por ser simples, rápido e possuir um plugin 
para maven para que o mesmo seja iniciado automaticamente após o build.
Também foi utilizado o plugin jacoco para poder gerar as métricas dos testes unitários 
e disponibilizar os resultados em uma página html.

### Instalação 

Basta baixar o código fonte, configurar o maven na máquina onde irá rodar o servidor 
e executar o comando mvn jetty:run no diretório raiz da aplicação.


### Rodando a suíte de testes

Para rodar os testes unitários basta utilizar o comando mvn verify, após a finalização 
deste, é criado um relatório em html na pasta target/site/jacoco.


### Consumindo os serviços

Para consumir os serviços basta utilizar via GET as seguintes URLS:

http://localhost:8080/xy-inc-correios/rest/enderecoService/buscarEnderecoCEP/{CEP valido}
http://localhost:8080/xy-inc-correios/rest/enderecoService/buscarCEPLogradouro/{logradouro}

A tratativa de erros é realizada via códigos http. Um arquivo de log é criado ao executar
a aplicação.
