package com.piccologrupa.zamjenasoba.domain;

import javax.persistence.*;

@Entity(name = "soba")
public class Soba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSoba;

    @OneToOne
    private Student idStudenta;

    @ManyToOne
    private Dom idDoma;

    private short paviljon;
    private short kat;
    private short kategorijaSobe;

    public Soba(Long idSoba, Student idStudenta, Dom idDoma, short paviljon, short kat, short kategorijaSobe) {
        this.idSoba = idSoba;
        this.idStudenta = idStudenta;
        this.idDoma = idDoma;
        this.paviljon = paviljon;
        this.kat = kat;
        this.kategorijaSobe = kategorijaSobe;
    }

    public Soba(){
        super();
    }

    public Long getIdSoba() {
        return idSoba;
    }

    public void setIdSoba(Long idSoba) {
        this.idSoba = idSoba;
    }

    public Student getIdStudenta() {
        return idStudenta;
    }

    public void setIdStudenta(Student idStudenta) {
        this.idStudenta = idStudenta;
    }

    public Dom getIdDoma() {
        return idDoma;
    }

    public void setIdDoma(Dom idDoma) {
        this.idDoma = idDoma;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Soba soba = (Soba) o;

        if (getPaviljon() != soba.getPaviljon()) return false;
        if (getKat() != soba.getKat()) return false;
        if (getKategorijaSobe() != soba.getKategorijaSobe()) return false;
        if (!getIdSoba().equals(soba.getIdSoba())) return false;
        if (!getIdStudenta().equals(soba.getIdStudenta())) return false;
        if (!getIdDoma().equals(soba.getIdDoma())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getIdSoba().hashCode();
        result = 31 * result + getIdStudenta().hashCode();
        result = 31 * result + getIdDoma().hashCode();
        result = 31 * result + (int) getPaviljon();
        result = 31 * result + (int) getKat();
        result = 31 * result + (int) getKategorijaSobe();
        return result;
    }

    public boolean hasFeatures(Dom dom, short paviljon, short kat, short kategorijaSobe) {
        if (dom != null && dom.getIdDom() != idDoma.getIdDom()) return false;
        if (paviljon != 0 && paviljon != this.paviljon) return false;
        if (kat != 0 && kat != this.kat) return false;
        if (kategorijaSobe != 0 && kategorijaSobe != this.kategorijaSobe) return false;
        return true;
    }

}
