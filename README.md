# Formulário (Simples) com Lista de Convidados com Spring Framework
> Este projeto consiste na aplicação simples de uma ferramenta de gestão de um evento, nele foram utilizadas as tecnologias Spring Boot, Spring MVC, Spring Data, Spring Security e Thymeleaf, utilizando a facilidade do Spring Tool Suíte (STS) da IDE Eclipse, com integração com o gerenciador de banco de dados MySQL e com o gerenciamento de dependências realizadas com o Maven. 

[![NPM Version][npm-image]][npm-url]
[![Build Status][travis-image]][travis-url]
[![Downloads Stats][npm-downloads]][npm-url]

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

/* 
 * Não é preciso inserir a anotação @Repository pois estamos estendendo a interface JpaRepository
 */
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

@Entity // Identifica para o Spring Data (JPA) que a Classe é uma representante de uma Entidade (BD)
public class Convidado implements Serializable{
	
	@Id  // Identifica que é o ID (PK) da Entidade do Banco de Dados
	@GeneratedValue
	private Long id;
	
	private String nome;
	
	private Integer quantidadeAcompanhantes;

	// métodos getters and setters dos atributos de classe

}
```

As anotações @Entity torna a classe uma representante (entidade) do banco de dados, já as anotações @Id e @GeneratedValue são para marcar a propriedade como identificador (corresponde a Chave Primária (PK) na tabela) e solicitar que o Hibernate gerencie o incremento do ID automaticamente, respectivamente.



## Exemplo de uso

Alguns exemplos interessantes e úteis sobre como seu projeto pode ser utilizado. Adicione blocos de códigos e, se necessário, screenshots.

_Para mais exemplos, consulte a [Wiki][wiki]._ 

## Configuração para Desenvolvimento

Descreva como instalar todas as dependências para desenvolvimento e como rodar um test-suite automatizado de algum tipo. Se necessário, faça isso para múltiplas plataformas.

```sh
make install
npm test
```


## Contributing

1. Faça o _fork_ do projeto (<https://github.com/yourname/yourproject/fork>)
2. Crie uma _branch_ para sua modificação (`git checkout -b feature/fooBar`)
3. Faça o _commit_ (`git commit -am 'Add some fooBar'`)
4. _Push_ (`git push origin feature/fooBar`)
5. Crie um novo _Pull Request_

[npm-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square
[npm-url]: https://spring.io/
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/seunome/seuprojeto/wiki