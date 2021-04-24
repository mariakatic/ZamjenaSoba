package com.piccologrupa.zamjenasoba.domain;

import java.util.Set;

import javax.persistence.*;

@Entity(name = "oglas")
public class Oglas implements Comparable<Oglas> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOglas;
	
	@ManyToOne
	private Student student;

	@ManyToOne
	private Dom dom;
	
	@OneToMany(mappedBy = "oglas")
	Set<LajkaniOglas> lajkovi;

	private short paviljon;
	private short kat;
	private short kategorijaSobe;
	private boolean aktivan;

	public Oglas(Student student, com.piccologrupa.zamjenasoba.domain.Dom dom, short paviljon, short kat, short kategorijaSobe) {
		this.student = student;
		this.dom = dom;
		this.paviljon = paviljon;
		this.kat = kat;
		this.kategorijaSobe = kategorijaSobe;
		this.aktivan = true;
	}

	//	public Oglas(Soba vlastitaSoba, Dom zeljeniDom, short zeljeniPaviljon, short zeljeniKat, short zeljenaKategorijaSobe) {
//		super();
//		this.vlastitaSoba = vlastitaSoba;
//		this.zeljeniDom = zeljeniDom;
//		this.zeljeniPaviljon = zeljeniPaviljon;
//		this.zeljeniKat = zeljeniKat;
//		this.zeljenaKategorijaSobe = zeljenaKategorijaSobe;
//		this.aktivan = true;
//		this.nePrikazuj = new HashSet<>();
//	}

	public Oglas() {
		super();
	}

	public Long getIdOglas() {
		return idOglas;
	}

	public void setIdOglas(Long idOglas) {
		this.idOglas = idOglas;
	}

//	public Soba getVlastitaSoba() {
//		return vlastitaSoba;
//	}
//
//	public void setVlastitaSoba(Soba vlastitaSoba) {
//		this.vlastitaSoba = vlastitaSoba;
//	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public com.piccologrupa.zamjenasoba.domain.Dom getDom() {
		return dom;
	}

	public void setDom(com.piccologrupa.zamjenasoba.domain.Dom dom) {
		this.dom = dom;
	}

	public short getPaviljon() {
		return paviljon;
	}

	public void setPaviljon(short paviljon) {
		this.paviljon = paviljon;
	}

	public short getKat() {
		return kat;
	}

	public void setKat(short kat) {
		this.kat = kat;
	}

	public short getKategorijaSobe() {
		return kategorijaSobe;
	}

	public void setKategorijaSobe(short kategorijaSobe) {
		this.kategorijaSobe = kategorijaSobe;
	}

	public boolean isAktivan() {
		return aktivan;
	}

	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}

	@Override
	public int compareTo(Oglas o) {
		return this.getIdOglas().compareTo(o.getIdOglas());
	}

//	public static boolean match(Oglas o1, Oglas o2) {
//		if (!o1.vlastitaSoba.hasFeatures(o2.zeljeniDom, o2.zeljeniPaviljon, o2.zeljeniKat, o2.zeljenaKategorijaSobe)) return false;
//		if (!o2.vlastitaSoba.hasFeatures(o1.zeljeniDom, o1.zeljeniPaviljon, o1.zeljeniKat, o1.zeljenaKategorijaSobe)) return false;
//		return true;
//	}

//	public void addNePrikazuj(Student student) {
//		this.nePrikazuj.add(student);
//	}

//	public Set<Student> getNePrikazuj() {
//		return this.nePrikazuj;
//	}

}
