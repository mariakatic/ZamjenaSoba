package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "Filter")
public class Filter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFilter;
	
	private String dom;
	
	@OneToOne
	private Student student;
	
	private int kat;
	private int kategorijaSobe;
	private int paviljon;
	
	
	
	public Filter() {
		super();
	}

	public Filter(String dom, Student student, int kat, int kategorijaSobe, int paviljon) {
		super();
		this.dom = dom;
		this.student = student;
		this.kat = kat;
		this.kategorijaSobe = kategorijaSobe;
		this.paviljon = paviljon;
	}

	public Long getIdFilter() {
		return idFilter;
	}

	public void setIdFilter(Long idFilter) {
		this.idFilter = idFilter;
	}

	public String getDom() {
		return dom;
	}

	public void setDom(String dom) {
		this.dom = dom;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getKat() {
		return kat;
	}

	public void setKat(int kat) {
		this.kat = kat;
	}

	public int getKategorijaSobe() {
		return kategorijaSobe;
	}

	public void setKategorijaSobe(int kategorijaSobe) {
		this.kategorijaSobe = kategorijaSobe;
	}

	public int getPaviljon() {
		return paviljon;
	}

	public void setPaviljon(int paviljon) {
		this.paviljon = paviljon;
	}
	
	
	
	

}
