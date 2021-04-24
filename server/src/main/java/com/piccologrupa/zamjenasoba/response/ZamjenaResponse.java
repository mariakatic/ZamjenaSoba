package com.piccologrupa.zamjenasoba.response;

import com.piccologrupa.zamjenasoba.domain.MogucaZamjena;
import com.piccologrupa.zamjenasoba.domain.Oglas;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class ZamjenaResponse {

    private Long idLanca;

    private Short redniBroj;

    private Oglas oglas;

    private boolean confirmed;

    public ZamjenaResponse() {}

    public ZamjenaResponse(MogucaZamjena zamjena, boolean confirmed) {
        this.idLanca = zamjena.getIdLanca();
        this.redniBroj = zamjena.getRedniBroj();
        this.oglas = zamjena.getOglas();
        this.confirmed = confirmed;
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
