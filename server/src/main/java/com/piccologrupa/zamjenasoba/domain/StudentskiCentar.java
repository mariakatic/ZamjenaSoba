package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.*;

@Entity(name = "SC")
public class StudentskiCentar {
	
	@Id
	@GeneratedValue
	private Long idSC;
	
	private String ime;
	
	@ManyToOne
	private Grad grad;
	
	public StudentskiCentar(String ime, Grad grad) {
		super();
		this.ime = ime;
		this.grad = grad;
	}
	
	public StudentskiCentar() {
		super();
	}

	public Long getIdSC() {
		return idSC;
	}

	public void setIdSC(Long idSC) {
		this.idSC = idSC;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	
	

}
