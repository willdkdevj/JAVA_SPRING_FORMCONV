package br.com.supernova.convidados.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.supernova.convidados.model.Convidado;
import br.com.supernova.convidados.repository.Convidados;

//Como as requisições (GET/POST) apontam para o mesmo destino é utilizada esta anotação para unificar
@Controller
@RequestMapping("/convidados")
public class ConvidadosController {
	
	// Anotação que injeta o contexto da interface (Repository) no contexto do Controller (Spring MVC)
	@Autowired
	private Convidados convidados;
	
	/*
	 * O método retorna um objeto do tipo ModelAndView que retorna um objeto da Controller
	 * e uma String que representa o nome da View
	 */
	// Como foi utilizada a anotação @RequestMapping foi omitido o destino da URL pois é o mesmo de POST
	@GetMapping
	public ModelAndView returnedList() {
		ModelAndView mav = new ModelAndView("listaconvidados"); // Adicionado a view no construtor de ModelAndView
		mav.addObject(new Convidado());  // Adicionado um objeto Convidado ao ModelAndView
		mav.addObject("convidados", convidados.findAll());   // Adicionado a Lista de Convidados no ModelAndView (Repository)
		return mav;
	}
	// Como foi utilizada a anotação @RequestMapping foi omitido o destino da URL pois é o mesmo de GET
	@PostMapping
	public String savedObjects(Convidado convidado) {
		this.convidados.save(convidado);
		return "redirect:/convidados";
	}
}
