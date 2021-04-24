package com.piccologrupa.zamjenasoba.response;

public class FilterResponse {
	
	private String dom;
	private int kat;
	private int kategorijaSobe;
	private int paviljon;
	
	public FilterResponse(String dom, int kat, int kategorijaSobe, int paviljon) {
		super();
		this.dom = dom;
		this.kat = kat;
		this.kategorijaSobe = kategorijaSobe;
		this.paviljon = paviljon;
	}

	public String getDom() {
		return dom;
	}

	public void setDom(String dom) {
		this.dom = dom;
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
