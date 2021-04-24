package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.*;

@Entity(name = "dom")
public class Dom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDom;
	
	private String imeDoma;

	@ManyToOne
	private Grad grad;
	
	@ManyToOne
	private StudentskiCentar sc;
	
	@ManyToOne
	private Menza najblizaMenza;

	public Dom(String imeDoma, Grad grad, StudentskiCentar sc,
			Menza najblizaMenza) {
		super();
		this.imeDoma = imeDoma;
		this.grad = grad;
		this.sc = sc;
		this.najblizaMenza = najblizaMenza;
	}

	public Dom() {
		super();
	}

	public Long getIdDom() {
		return idDom;
	}

	public void setIdDom(Long idDom) {
		this.idDom = idDom;
	}

	public String getImeDoma() {
		return imeDoma;
	}

	public void setImeDoma(String imeDoma) {
		this.imeDoma = imeDoma;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}

	public StudentskiCentar getSc() {
		return sc;
	}

	public void setSc(StudentskiCentar sc) {
		this.sc = sc;
	}

	public Menza getNajblizaMenza() {
		return najblizaMenza;
	}

	public void setNajblizaMenza(Menza najblizaMenza) {
		this.najblizaMenza = najblizaMenza;
	}
	
	

}
