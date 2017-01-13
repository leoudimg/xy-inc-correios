<html>
<head>
<title>Teste servicos correios</title>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/style.css">
</head>

<body>

	<script type="text/javascript">
	function consultarCEP(){
		
		var url = "http://localhost:8080/xy-inc-correios/rest/enderecoService/buscarEnderecoCEP/" + encodeURI($("#CEP").val());
		$.get( url)
		  .done(function( data ) {
			  
		   var html = "<table class='tmptabela'><tr><th width='150'>Logradouro/Nome:</th><th width='90'>Bairro/Distrito:</th><th width='80'>Localidade/UF:</th><th width='50'>CEP:</th></tr>";
           
		   for(i = 0; i < data.length; i++){
		   
			   html += "<tr>"
			   html += "<td width='150'>" + data[i].logradouro + '</td>';
			   html += "<td width='90'>" + data[i].bairro + '</td>';
			   html += "<td width='80'>" + data[i].uf + '</td>';
			   html += "<td width='55'>" + data[i].cep + '</td>';
			   html += "</tr>";
		   }
		   
		   html += "</table>";
		   
		   $("#divCEP .tmptabela").remove()
		   $("#divCEP").append(html);
           
		  });
	}
	
	
function consultarLogradouro(){
		
		var url = "http://localhost:8080/xy-inc-correios/rest/enderecoService/buscarCEPLogradouro/" + encodeURI($("#logradouro").val());
		$.get( url)
		  .done(function( data ) {
			  
			var html = "";
			  
			for(i = 0; i < data.length; i++){
				html += data[i] + "<br>";
			}
		   
		   $("#conteudo").html(html);
           
		  });
	}

	</script>

	<div class="header">Teste API Rest</div>
	<div class="wrapper">
		<div id="divCEP">
			<h3>Consultar CEP</h3>
			<input type="text" id="CEP" placeholder="CEP" size="50" maxlength="255" value="" />
			<button onClick="consultarCEP();">Pesquisar</button>
		</div>
		
		<div id="divLogradouro">
			<h3>Consultar Logradouro</h3>
			<input type="text" id="logradouro" placeholder="Logradouro" size="50" maxlength="255" value="" />
			<button onClick="consultarLogradouro();">Pesquisar</button>
			<div id="conteudo"></div>
		</div>


	</div>
</body>
</html>