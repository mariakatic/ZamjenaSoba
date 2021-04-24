package com.piccologrupa.zamjenasoba.response;

import com.piccologrupa.zamjenasoba.domain.Oglas;

public class OglasResponse {

    private Oglas oglas;
    private int stupanjLajkanja;

    public OglasResponse(Oglas oglas, int stupanjLajkanja) {
        this.oglas = oglas;
        this.stupanjLajkanja = stupanjLajkanja;
    }

    public OglasResponse(){}

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
}
