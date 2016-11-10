package br.com.wallstreet.vraptor.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.wallstreet.vraptor.entity.Acao;

@Controller
public class AcaoController {
	
	private EntityManager entityManager;
	private final Result result;
	
	/**
	 * @deprecated
	 */
	public AcaoController() {
		this(null, null);
	}
	
	@Inject
	public AcaoController(EntityManager entityManager,
			              Result result) {
		this.entityManager = entityManager;
		this.result = result;
	}
	
	@Get("/json/acoes")
	public void listarAcoes() {
		List<Acao> acoes = 
				this.entityManager
				.createQuery("select a from Acao a", Acao.class).getResultList();
		
		this.result.use(Results.json()).from(acoes).serialize();
	}
	
	@Get("/json/melhoresAcoes")
	public void listarMelhoresAcoes() {
		List<Acao> acoes = 
				this.entityManager
				.createQuery("select a from Acao a order by a.valor desc", Acao.class)
				.setMaxResults(5)
				.getResultList();
		
		this.result.use(Results.json()).from(acoes).serialize();
	}
	
	@Get("/json/pioresAcoes")
	public void listarPioresAcoes() {
		List<Acao> acoes = 
				this.entityManager
				.createQuery("select a from Acao a order by a.valor", Acao.class)
				.setMaxResults(5)
				.getResultList();
		
		this.result.use(Results.json()).from(acoes).serialize();
	}
	
	@Transactional
	@Post("/cadastro/acao")
	public void criarAcaoManual(Acao acao) {
		entityManager.persist(acao);
		this.result.nothing();
	}
	
	@Transactional
	@Post("/gerar/acao")
	public void criarAcaoAutomatico() {
		Random random = new Random();
		Acao acao = new Acao();
		acao.setNomeEmpresa("Empresa " + Math.abs( random.nextInt() % 10000 ));
		acao.setValor( BigDecimal.valueOf(random.nextDouble() * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() );
		entityManager.persist(acao);
		this.result.nothing();
	}
	
}
