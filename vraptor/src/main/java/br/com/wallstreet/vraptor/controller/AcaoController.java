package br.com.wallstreet.vraptor.controller;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.wallstreet.vraptor.entity.Acao;

@Controller
public class AcaoController {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private final Result result;
	
	/**
	 * @deprecated
	 */
	public AcaoController() {
		this(null);
	}
	
	@Inject
	public AcaoController(Result result) {
		this.result = result;
	}
	
	@Get("/json/acoes")
	public void listarAcoes() {
		List<Acao> acoes = 
				this.entityManager
				.createQuery("select a from Acao a", Acao.class).getResultList();
		
		this.result.use(Results.json()).from(acoes).serialize();
	}
	
}
