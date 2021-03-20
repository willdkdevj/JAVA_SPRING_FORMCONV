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
			.withUser("william").password("{noop}dkrock").roles("ADMIN")
			.and()
			.withUser("derek").password("{noop}123").roles("USER")
			.and()
			.withUser("convidado").password("{noop}123456").roles("USER");
	}
}
