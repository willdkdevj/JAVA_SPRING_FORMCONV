# Formulário (Simples) com Lista de Convidados com Spring Framework
> Este projeto consiste na aplicação simples de uma ferramenta de gestão de um evento, nele foram utilizadas as tecnologias Spring Boot, Spring MVC, Spring Data, Spring Security e Thymeleaf, utilizando a facilidade do Spring Tool Suíte (STS) da IDE Eclipse, com integração com o gerenciador de banco de dados MySQL e com o gerenciamento de dependências realizadas com o Maven. 

[![Spring Badge](https://img.shields.io/badge/-Spring-brightgreen?style=flat-square&logo=Spring&logoColor=white&link=https://spring.io/)](https://spring.io/)
[![Maven Badge](https://img.shields.io/badge/-MAVEN-000?style=flat-square&logo=MAVEN&logoColor=white&link=https://maven.apache.org/)](https://maven.apache.org/)
[![MySQL Badge](https://img.shields.io/badge/-MySQL-blue?style=flat-square&logo=MySQL&logoColor=white&link=https://www.mysql.com/)](https://www.mysql.com/)


<img align="right" width="400" height="300" src="https://matheuspcarvalhoblog.files.wordpress.com/2018/05/spring-framework.png">

## Descrição da Aplicação
A aplicação é bem simples, consiste na gestão de nomes de pessoas convidadas para um evento e a quantidade de acompanhantes que ele levará para o mesmo.
A aplicação consiste em uma única tela com dois campos de entrada e texto, um para o nome e outro para a quantidade, respectivamente. Além disso, terá um botão de adicionar mais convidados a lista que é uma tabela a fim de mostrar quem já foi cadastrado.

No decorrer deste documento é apresentado com mais detalhes sua implementação, descrevendo como foi atribuido o Spring MVC na segmentação de  nossa aplicação a fim de definir camadas de responsabilidades para tratar o fluxo de requisições, facilitando a manutenção do código. Como implementamos o Spring Data JPA que nos dá diversas funcionalidades tornando simples a dinamica de operações com banco de dados e sua manutenção. Como foi aplicado o Spring Security a fim de colocar uma camada de segurança a aplicação permitindo só que tenha uma autentificação e autorização possa realizar modificações a ela através do banco de dados, LDAP ou memória. Já o Spring Boot trás a agilidade ao analisar nosso código e configurá-lo conforme nossas necessidades por meio dos *starters* agrupando as dependências.

## Importação do Projeto Maven para Execução da Aplicação
O Apache Maven é uma ferramenta de apoio a equipes que trabalham com projeto Java (mas não se restringe somente a Java), possibilitando a desenvolvedores a automatizar, gerenciar e padronizar a construção e publicação de suas aplicações, permitindo maior agilidade e qualidade ao produto.
Abaixo são apresentadas as etapas para importá-lo a IDE Eclipe para já analisar seu funcionamento, mas também é possível trabalhar com outras IDE's como IntelliJ, NetBeans, entre outras, podendo ser diferente os procedimentos realizados.

1. No menu principal, acesse File >> Import;
2. Selecione a opção "Existing Maven Projects", e clique em *Next*;
3. Forneça o caminho de onde você realizou o ``git clone``, na qual consiste no diretório do projeto, e clique em *Finish*;
4. Agora devemos sincronizar as configurações do projeto com as do pom.xml, clique com o botão direito do ``mouse`` no projeto e selecione a opção Maven >> Update Project;
5. Iremos "forçar" a atualização do snapshots e releases deixando as opções:
	* Force Update of Snapshots/Releases;
	* Update project configuration from pom.xml;
	* Refresh workspace resources from local filesystem;
	* Clean projects.
Depois de selecionar todas as opções, clique no botão *OK*;
6. Vamos limpar os arquivos de compilação ao clicar novamente com o botão direito do ``mouse`` no projeto e selecionar a opção Run As >> Maven clean;
7. E repetir o mesmo procedimento anterior, mas agora vamos selecionar Run As >> Maven install;
8. Para finalizar e ver o projeto rodando execute o mesmo procedimento anterior, mas agora iremos selecionar Run As >> Spring Boot App.

Se tudo der certo a aplicação Spring será executada pelo seguinte endereço (http://localhost:8080/convidados)[http://localhost:8080/convidados].

## Como Foi Configurado o Projeto Spring no Eclipse
O projeto Spring foi criado a partir do Spring Tool Suíte (STS), que é um ferramenta de apoio que foi integrada ao IDE Eclipse, mas também é possível realizar a criação do projeto fora da IDE, através do (Spring Initializr)[https://start.spring.io/] que é uma interface web (site) que fornece as mesmas funcionalidades de estruturar as configurações pré-moldadas ao projeto.

> No Eclipse utilizamos o caminho File >> New >> Spring Starter Project.
> No Spring Initializr após a configuração moldada será exportada um pacote zip que deve ser importado pela sua IDE favorita.

Vamos para as configurações que foram utilizadas:
* No campo Name, foi informado o nome do projeto, que foi listaconvidados;
* Em Type, foi selecionado Maven;
* Em Packaging, foi utilizada a opção Jar;
* Para Java Version, foi selecionado a versão do Java, na qual em meu computador estava a versão 11 (recomendo que você use a versão 1.8 ou superior para este projeto);
* Em Language, marcado o Java
Já as configurações de ``Group``, ``Artifact`` e ``Version`` são informações do Maven para identificar o projeto Spring.
* Em Group, foi informado o domínio da empresa conforme conversão para identificação, por exemplo, br.com.estudojavaspring;
* Em Artifact, foi informado o nome do aplicativo gestao-convidados
* Em Version, foi informado 0.0.1-SNAPSHOT.
> A palavra SNAPSHOT, no contexto de um projeto, significa que estamos na versão de desenvolvimento, e que se gerarmos um jar ou war, teremos apenas um estático do projeto até aquele ponto, e não uma versão final.
* Se quiser adicionar uma descrição sobre o que é o projeto, utilize o campo Description.
* E em Package, definimos o nome do pacote que deve ser gerado para a aplicação. Desta forma, pode-se seguir o mesmo modelo do domínio mais o nome do projeto: br.com.estudojavaspring.listaconvidados.
Depois esta configuração inicial foi selecinado os frameworks necessários para continuarmos a implementação do projeto. Desta forma utilizamos o recurso de pesquisa para selecinarmos:
* DevTools;
* JPA;
* H2;
* Thymeleaf;
* Web.

Desta forma o starters já implementa um projeto praticamente funcional, onde encontramos a classe principal do nosso projeto em ``src/main/java`` com o pacote que configuramos pelo STS ou Initializr ``br.com.estudojavaspring.listaconvidados``.
```sh
package br.com.estudojavaspring.listaconvidados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestaoFestaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoFestaApplication.class, args);
	}
}
```
A anotação @SpringBootApplication informa que a classe pertence as configurações do Spring, além de definir o ponto de partida para a procura de mais componentes relacionados a aplicação, desta forma, todas as classes devem seguir a partir deste pacote para serem mapeados pelo Spring.

### Spring MVC - Camada Controller
A partir daqui podemos criar a estrutura MVC, que consiste na separação explicita das responsabilidades de cada componente do fluxo de requisições. O MVC é um acrônimo de Model, View e Controller que visa separar quem representa o objeto relacional (tabela do BD) que contém as informações da requisição, quem será reponsável por apresentar esta lógica ao cliente e quem terá a lógica de negócio para resolver uma requisição, respectivamente.

Desta forma, a partir desta lógica foi criado o pacote controller em ``br.com.estudojavaspring.listaconvidados`` com a classe ConvidadoController.
```sh
package br.com.estudojavaspring.listaconvidados.controller;

import org.springframework.stereotype.Controller;

@Controller
public class ConvidadosController {
}
```

E a partir dela foram criados os métodos para receber as requisições do cliente através dos métodos GET e POST do protocolo HTTP.
```sh
	@GetMapping
	public ModelAndView returnedList() {
		ModelAndView mav = new ModelAndView("listaconvidados"); 
		mav.addObject(new Convidado());  
		mav.addObject("convidados", convidados.findAll());   
		return mav;
	}
	
	@PostMapping
	public String savedObjects(Convidado convidado) {
		this.convidados.save(convidado);
		return "redirect:/convidados";
	}
```
O método anotado com @GetMapping informa para qual URL deve ser mapeada as requisições do tido GET, assim como, a anotação @PostMapping para receber requisições do tipo POST, mas elas estão sem a "rota" em seus atributos, isto ocorre porque como as duas recebem requisições da mesma página, desta forma, é possível unificá-las em uma única anotação omitindo desta maneira sua URL mais deixando-a explícita na classe, através da anotação @RequestMapping
```sh
@Controller
@RequestMapping("/convidados")
public class ConvidadosController {
}
```
Voltando para os métodos, o método returnedList() retorna um objeto do tipo ModelAndView onde antes de retorná-lo foram adicionado View (listaconvidados.html) para o seu construtor e na sequencia foram adicionados um Objeto do tipo JPA (Entity), um repositório (através da injeção de inversão de controle) com uma lista de todos os objetos JPA encontrados.

Já o método savedObjects() recebe um Convidado através do conceito do Thymeleaf ... na qual é passado para o objeto *Repository* que foi injetado pela interface utilizando o método herdado de JpaRepository, o save(), para adicionado no contexto JPA e redirecionando para a mesma página através da _String_ "redirect:/convidados". Desta forma, é atualizada a página apresentando mais um item a lista (tabela) da view listaconvidados e está mapeada em /convidados.

Também foi injetado a dependência (Dependency Injection - DI) ao contexto MVC ao implementarmos o objeto Convidados que é uma interface, na qual estende a JpaRepository, onde ao utilizar a anotação @Autowired que avisa ao Spring Framework para injetar uma instância de Convidado.
```sh
@Autowired
private Convidados convidados;
``` 

Isto é possível por que a interface Convidados estende a interface JpaRepository, que possuí alguns métodos que fornecem vários recursos para trabalhar com ORM. Vejamos sua implementação (Note que foi criado o pacote repository para colocar a interface):
```sh
package br.com.estudojavaspring.listaconvidados.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.supernova.convidados.model.Convidado;

public interface Convidados extends JpaRepository<Convidado, Long>{

}
```

### Spring MVC - Camada Model
Seguindo o conceito do MVC também foi criado o pacote model em ``br.com.estudojavaspring.listaconvidados`` com a classe Convidado. Onde novamente, aplica-se o JPA/Hibernate para realizar as operações de CRUD em nosso banco de dados, sendo que cada classe representa uma entidade (tabela)
do mesmo. O código ficou da seguinte forma:
```sh
package br.com.estudojavaspring.listaconvidados.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity 
public class Convidado implements Serializable{
	
	@Id  
	@GeneratedValue
	private Long id;
	
	private String nome;
	
	private Integer quantidadeAcompanhantes;

	// métodos getters and setters dos atributos de classe

}
```

As anotações @Entity torna a classe uma representante (entidade) do banco de dados, já as anotações @Id e @GeneratedValue são para marcar a propriedade como identificador (corresponde a Chave Primária (PK) na tabela) e solicitar que o Hibernate gerencie o incremento do ID automaticamente, respectivamente.

### Spring MVC - Camada View (com Thymeleaf)
A anotação @GetMapping presente na classe ConvidadosController do pacote controller, indica ao método que responda a requisição HTTP (GET) para view convidados, ao criar o objeto ModelAndView foi passado em seu construtor o nome do arquivo HTML a ser retornado ao cliente, denominado ``listaconvidados``.
```sh
	ModelAndView mav = new ModelAndView("listaconvidados");
```

Desta forma, foi desenvolvido este arquivo implementando com outras tecnologias a fim de torná-lo responsivo a qualquer tipo de dispositivo utilizando o Bootstrap no Thymeleaf, que recebe os objetos Java enviados pelo Controller convertendo-os em HTML "puro". O Thymeleaf não é vigente do Spring Framework, mas é uma biblioteca que foi criada para facilitar a criação de camada de View, sendo uma alternativa ao uso de Java Server Pages (JSP).

A configuração (default) do Spring Boot implementado com a biblioteca do Thymeleaf, define que a View deve ficar no seguinte pacote ``src/main/resources/templates``, ao invés de criarmos um pacote view no caminho do projeto Spring. Desta forma, ao criar o arquivo HTML definidos a seguinte estrutura para o documento:
```sh
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	  xmlns:th="http://www.thymeleaf.org">
<head>
	<meta charset="UTF-8"/>
	<meta name="viewport" content="width=device-width" />
	<title>Lista de Convidados</title>
	<link th:href="@{/webjars/bootstrap/css/bootstrap.min.css}" rel="stylesheet"/>
	<link th:href="@{/webjars/bootstrap/css/bootstrap-theme.min.css}" rel="stylesheet"/>
</head
```

O ``xmlns`` presente na tag ``<html>`` especifica o *namespace* XML ao documento, que significa que sempre deverá ser aberto e fechado as tag's HTML por se tratar de um documento do tipo XHTML, note que também existe o ``xmlns:th`` que define que será permitido o uso das propriedades do Thymeleaf.

A importação do Bootstrap, utiliza a biblioteca WebJars, para que não seja necessário realizar o download para dentro do projeto. O CSS do Bootstrap utilizado fica dentro do JAR do WebJars, que é colocado como dependência do projeto através do Maven. Desta maneira, foi inserido as dependências ``org.webjars.bootstrap`` e ``org.webjars.webjars-locator`` no arquivo pom.xml, que possibilita que não seja necessário a inclusão da versão do Bootstrap na URL usada para importar o CSS.
```sh
<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>webjars-locator</artifactId>
</dependency>
<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>bootstrap</artifactId>
	<version>3.3.7</version>
</dependency>
```

Também é inserido antes de fechar a tag ``<body>`` da página, essas duas importações de JavaScript:
```sh
<script th:src="@{/webjars/jquery/jquery.min.js}"></script>
<script th:src="@{/webjars/bootstrap/js/bootstrap.min.js}"></script>
```

São utilizados atributos ``th:href`` do Thymeleaf permitem usar links de endereçamento com a expressão ``@{}``. Sua vantagem é colocar o contexto da ``th:src`` na aplicação. Já a expressão ``${}`` interpreta variáveis locais ou disponibilizadas pelo controller. Em relação ao atributo ``th:each`` itera sobre a lista de convidados, atribuindo cada objeto na variável local ``convidado``, desta maneira, é possível iterar com vários elementos ``<tr>`` renderizando-os na página.
```sh

```

Dentro de cada <tr> existem 2 elementos <td>. A *string* a ser apresentada vem do atributo ``th:text``, trabalha em conjunto com a expressão ``${}``, ao ler as propriedades da variável local ``convidado`` que são provenientes da classe Convidados, presente em Model. Já o ``th:each``, usa da expressão adicionando o objeto de controller ``${convidados}`` para recuperar o objeto de Model, que possui uma lista de objetos.
```sh
<tr th:each="convidado : ${convidados}">
	<td th:text="${convidado.nome}"></td>
	<td th:text="${convidado.quantidadeAcompanhantes}"></td>
</tr>
```
Na elemento ``panel`` também foi implementado um formulário a fim de ser preenchido o nome e a quantidade de acompanhantes de um convidado.
Abaixo segue o código adicionado ao documento HTML ``listaconvidados``
```sh
<form class="form-inline" method="POST" style="margin: 20px 0">
	<div class="form-group">
		<input type="text" class="form-control" placeholder="Nome"/>
		<input type="text" class="form-control" placeholder="Acompanhantes"/>
		<button type="submit" class="btn btn-primary">Adicionar</button>
	</div>
</form>
```
A primeira alteração será no método listar() do controller. Vamos adicionar um
objeto do tipo Convidado no ModelAndView .
O objeto criado em returnedList, o objeto ModelAndView é chamado de command object, que é o objeto que modela o formulário, ou seja, é ele que será setado com os valores das tags input da página. Para que o Thymeleaf possa usar este objeto no formulário, foi adicionado o atributo ``th:object`` na tag ``<form>``.
```sh
<form class="form-inline" method="post" th:object="${convidado}">
```
E nos campos de entrada, foi utilizado as propriedades do objeto “convidado” nos inputs, usando ``th:field``, utilizando a expressão ``*{}`` para selecionar a propriedade do objeto.
```sh
<input type="text" class="form-control" placeholder="Nome Completo" th:field="*{nome}" />
<input type="text" class="form-control" placeholder="Nº Acompanhantes" size="15" maxlength="4" th:field="*{quantidadeAcompanhantes}" />
```

Este objeto é do tipo Convidado e suas propriedades nome e quantidadeAcompanhantes estão ligadas aos elementos input do form.

Também é passado o parâmetro ``th:action="@{/convidados}"`` que orienta ao Thymeleaf em qual endereço deve enviar os dados.
```sh
<form class="form-inline" method="post" th:object="${convidado}" th:action="@{/convidados}" style="margin: 20px 0">
```

A expressão ``@{}`` é utilizada para utilizar os links no HTML, pois resolve o ``context path`` da aplicação automaticamente, permitindo desta forma, utilizar o método savedConvidado anotado com o @PostMapping na classe Controller.

### Aplicando Segurança a Aplicação Através do Spring Security
Para aplicar um nível de segurança a aplicação, utilizamos o Spring Security, que fornece, em conjunto com o Spring Boot, já é implementada a aplicação ao implmentá-lo no arquivo pom.xml. Desta forma, segue a dependência necessária a ser incluída:
```sh
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Para criar usuários para administrar, gerenciar ou operar a aplicação foi incluído alguns usuários através da criação da classe ``InMemorySecurityConfig`` criando o pacote ``config`` no pacote padrão da aplicação
```sh
package br.com.supernova.convidados.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
public class InMemorySegurityConfig {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder
			.inMemoryAuthentication()
			.withUser("william").password("{noop}_senha_).roles("ADMIN")
			.and()
			.withUser("derek").password("{noop}_senha_").roles("USER")
			.and()
			.withUser("convidado").password("{noop}_senha_").roles("USER");
	}
}
```

A anotação @Configuration torna a classe ``InMemorySecurityConfig``, onde esta classe pode configurar quaisquer configurações do Spring, mas a utilizamos para autenticar usuários.
O método configureGlobal, anotado com @Autowired, é uma convenção adotada para configurar o ``AuthenticationManagerBuilder``, na qual criamos três usuários para autenticar e parametrizar seu nível de autorização dentro da aplicação.
Para utilizarmos o Spring Security é necessário reiniciar o servidor Spring (TOMCAT-Embedado).

### O Uso do MySQL
Ao utilizar o JPA (através do Spring Data) podemos utilizar para configurar e até mesmo trocar o banco de dados, no ínicio do projeto foi incluso o H2, que é um banco que utiliza a memória para instanciar dados, mas é utilizada somente para testes. Para a aplicação foi selecionado o SGBD MySQL, mas poderia ser qualquer outro como o PostgreSQL, para isso, foi implementado o arquivo pom.xml com a dependẽncia do driver JDBC abaixo:
```sh
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
```

Para concluir, foi implementado o arquivo application.properties informado a URL de conexão do JDBC, usuário e senha, além de parametrizar o hibernate a criar e excluir o banco após reiniciar a aplicação. O caminho do arquivo encontra-se em ``src/main/resources/``: 
```sh
spring.datasource.url=jdbc:mysql://localhost/_nome_bancodados
spring.datasource.username=root
spring.datasource.password=_senha_banco
spring.jpa.hibernate.ddl-auto=create-drop
```

Utilizamos o banco ``supernova_tech`` criado no MySQL, sem tabelas ou qualquer outro objeto do banco, quanto ao usuário foi utilizado o root e a senha que foi atribuído a ele. Além disso, foi configurado a propriedade ``ddl-auto``, para recriar o banco de dados todas as vezes que o projeto se iniciar. 
> Esta configuração não pode ser implementada em Ambiente de Produção

## Contribuição

1. Faça o _fork_ do projeto (<https://github.com/willdkdevj/JAVA_SPRING_FORMCONV/fork>)
2. Crie uma _branch_ para sua modificação (`git checkout -b feature/fooBar`)
3. Faça o _commit_ (`git commit -am 'Add some fooBar'`)
4. _Push_ (`git push origin feature/fooBar`)
5. Crie um novo _Pull Request_

## Agradecimentos

Obrigado por ter acompanhado aos meus esforços para criar este Projeto utilizando o Maven e a estrutura do Spring! :octocat:

Como diria um antigo mestre:
> *"Cedo ou tarde, você vai aprender, assim como eu aprendi, que existe uma diferença entre CONHECER o caminho e TRILHAR o caminho."*
>
> *Morpheus - The Matrix*