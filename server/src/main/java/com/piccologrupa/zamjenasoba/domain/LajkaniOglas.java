package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.*;

@Entity(name = "lajkaniOglas")
public class LajkaniOglas {
	
	@EmbeddedId
	private LajkaniOglasKey idLajk;
	
	@ManyToOne
	@MapsId("id")
	@JoinColumn(name = "studentId")
	private Student student;
	
	@ManyToOne
    @MapsId("idOglas")
    @JoinColumn(name = "oglasId")
    private Oglas oglas;

    private int stupanjLajkanja;
    private boolean procitano;

	public LajkaniOglas(Student student, Oglas oglas, int stupanjLajkanja) {
		super();
		this.idLajk = new LajkaniOglasKey(student.getId(), oglas.getIdOglas());
		this.student = student;
		this.oglas = oglas;
		this.stupanjLajkanja = stupanjLajkanja;
		this.procitano = false;
	}

	public LajkaniOglas() {
		super();
	}

	public LajkaniOglasKey getIdLajk() {
		return idLajk;
	}

	public void setIdLajk(LajkaniOglasKey idLajk) {
		this.idLajk = idLajk;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Oglas getOglas() {
		return oglas;
	}

	public void setOglas(Oglas oglas) {
		this.oglas = oglas;
	}

	public int getStupanjLajkanja() {
		return stupanjLajkanja;
	}

	public void setStupanjLajkanja(int stupanjLajkanja) {
		this.stupanjLajkanja = stupanjLajkanja;
	}

	public boolean isProcitano() {
		return procitano;
	}

	public void setProcitano(boolean procitano) {
		this.procitano = procitano;
	}

}
