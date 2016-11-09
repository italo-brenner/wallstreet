package br.com.wallstreet.vraptor.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Acao {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idAcao;
	
	private String nomeEmpresa;
	
	private Double valor;
	
	public String getNomeEmpresa() {
		return nomeEmpresa;
	}
	
	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
}
