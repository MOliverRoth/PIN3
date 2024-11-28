Instruções para execução

-> Rodar o front-end:
* entrar na pasta do front (AplicationKeyControl) através do prompt de comando
* digitar o seguinte comando: npm run dev

-> preparar o Banco:
* Abrir o PGAdmin e criar um banco chamado keycontrol
* clicar com o botão direito no banco e realizar o Restore utilizando o arquivo tar "keycontrol-db" dentro da pasta do projeto
*atualizar as informações em application.properties dentro do caminho: pastadoprojeto\KeyControl\KeyControl\src\main\resources
	 ~ atualizar spring.datasource.username com o nome de usuário do postgres
	 ~ atualizar spring.datasource.password com a senha de usuário do postgres

-> Rodar o back-end:
* executar o arquivo KeyControlAplication.java


-> abrir em seu navegador de internet o link localhost://8081/
-> para utilizar o sistema, utilize os logins fornecidos em UsuariosDeTeste.txt
