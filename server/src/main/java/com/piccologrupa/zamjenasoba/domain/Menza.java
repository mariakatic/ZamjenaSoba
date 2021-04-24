package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.*;

@Entity(name = "menza")
public class Menza {
	
	@Id
	private String nazivMenze;
	
	@ManyToOne
	private Grad grad;

	public Menza(String nazivMenze, Grad grad) {
		super();
		this.nazivMenze = nazivMenze;
		this.grad = grad;
	}

	public Menza() {
		super();
	}

	public String getNazivMenze() {
		return nazivMenze;
	}

	public void setNazivMenze(String nazivMenze) {
		this.nazivMenze = nazivMenze;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	
	

}
