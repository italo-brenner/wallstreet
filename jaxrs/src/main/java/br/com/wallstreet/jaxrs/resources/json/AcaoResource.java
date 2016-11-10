package br.com.wallstreet.jaxrs.resources.json;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import br.com.wallstreet.jaxrs.entity.Acao;


@Stateless
@ApplicationPath("/resources/json")
@Path("/acao")
public class AcaoResource extends Application {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * @deprecated
	 */
	public AcaoResource() {
	}
	
	@GET
	@Path("/todasAcoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarAcoes(@HeaderParam("If-Modified-Since") Date modifiedSince) {
		List<Acao> acoes = 
				this.entityManager
				.createQuery("select a from Acao a", Acao.class).getResultList();
		
		if ( modifiedSince == null || algumaAcaoFoiAtualizada(acoes, modifiedSince) ) {
			return Response.ok(acoes).lastModified(new Date(System.currentTimeMillis())).build();
		} else {
			return Response.notModified().build();
		}
	}
	
	@GET
	@Path("/melhoresAcoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarMelhoresAcoes(@HeaderParam("If-Modified-Since") Date modifiedSince) {
		List<Acao> acoes = 
				this.entityManager
					.createQuery("select a from Acao a order by a.valor desc", Acao.class)
					.setMaxResults(5)
					.getResultList();
		
		if ( modifiedSince == null || algumaAcaoFoiAtualizada(acoes, modifiedSince)) {
			return Response.ok(acoes).lastModified(new Date(System.currentTimeMillis())).build();
		} else {
			return Response.notModified().build();
		}
	}
	
	@GET
	@Path("/pioresAcoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarPioresAcoes(@HeaderParam("If-Modified-Since") Date modifiedSince) {
		List<Acao> acoes = 
				this.entityManager
					.createQuery("select a from Acao a order by a.valor", Acao.class)
					.setMaxResults(5)
					.getResultList();

		if ( modifiedSince == null || algumaAcaoFoiAtualizada(acoes, modifiedSince)) {
			return Response.ok(acoes).lastModified(new Date(System.currentTimeMillis())).build();
		} else {
			return Response.notModified().build();
		}
	}
	
	@Transactional
	@POST
	@Path("/cadastro")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response criarAcaoManual(MultivaluedMap<String, String> params) {
		Acao acao = new Acao();
		acao.setNomeEmpresa(params.getFirst("nomeEmpresa"));
		acao.setValor(Double.valueOf(params.getFirst("valor")));
		acao.setDtAtualizacao(new Date(System.currentTimeMillis()));
		entityManager.persist(acao);
		
		return Response.status(
			javax.ws.rs.core.Response.Status.OK
		).build();
	}
	
	@Transactional
	@POST
	@Path("/gerar")
	public Response criarAcaoAutomatico() {
		Random random = new Random();
		Acao acao = new Acao();
		acao.setNomeEmpresa("Empresa " + Math.abs( random.nextInt() % 10000 ));
		acao.setValor( BigDecimal.valueOf(random.nextDouble() * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() );
		acao.setDtAtualizacao(new Date(System.currentTimeMillis()));
		entityManager.persist(acao);
		
		return Response.status(
			javax.ws.rs.core.Response.Status.OK
		).build();
	}
	
	private boolean algumaAcaoFoiAtualizada(List<Acao> acoes, Date dtReferencia) {
		for (Acao acao : acoes) {
			if (acao.getDtAtualizacao().after(dtReferencia)) {
				return true;
			}
		}
		return false;
	}
	
}
