package br.com.supernova.convidados.model;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQuantidadeAcompanhantes() {
		return quantidadeAcompanhantes;
	}

	public void setQuantidadeAcompanhantes(Integer quantidadeAcompanhantes) {
		this.quantidadeAcompanhantes = quantidadeAcompanhantes;
	}
	
}
