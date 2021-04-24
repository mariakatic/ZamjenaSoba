package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.*;

@Entity(name = "grad")
public class Grad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idGrada;

	private String imeGrada;

	public Grad(String imeGrada) {
		super();
		this.imeGrada = imeGrada;
	}

	public Grad() {
		super();
	}

	public String getImeGrada() {
		return imeGrada;
	}

	public void setImeGrada(String imeGrada) {
		this.imeGrada = imeGrada;
	}

	public int getIdGrada() {
		return idGrada;
	}

	public void setIdGrada(int idGrada) {
		this.idGrada = idGrada;
	}
}
