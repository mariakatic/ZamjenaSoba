package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.*;

@Entity(name = "mogucaZamjena")
@IdClass(ZamjenaID.class)
public class MogucaZamjena {
    @Id
    private Long idLanca;

    @Id
    private Short redniBroj;

    @ManyToOne
    private Oglas oglas;
    
    private boolean procitano;

    public MogucaZamjena() {}

    public MogucaZamjena(Long idLanca, Short redniBroj, Oglas oglas) {
        this.idLanca = idLanca;
        this.redniBroj = redniBroj;
        this.oglas = oglas;
        procitano = false;
    }

    public Long getIdLanca() {
        return idLanca;
    }

    public void setIdLanca(Long idLanca) {
        this.idLanca = idLanca;
    }

    public Short getRedniBroj() {
        return redniBroj;
    }

    public void setRedniBroj(Short redniBroj) {
        this.redniBroj = redniBroj;
    }

    public Oglas getOglas() {
        return oglas;
    }

    public void setOglas(Oglas oglas) {
        this.oglas = oglas;
    }

	public boolean isProcitano() {
		return procitano;
	}

	public void setProcitano(boolean procitano) {
		this.procitano = procitano;
	}
    
    
}