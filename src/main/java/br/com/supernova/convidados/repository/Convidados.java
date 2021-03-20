package br.com.supernova.convidados.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.supernova.convidados.model.Convidado;

/* 
 * Não é preciso inserir a anotação @Repository pois estamos estendendo a interface JpaRepository
 */
public interface Convidados extends JpaRepository<Convidado, Long>{

}
