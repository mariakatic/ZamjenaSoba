package com.piccologrupa.zamjenasoba.domain;

import java.io.Serializable;
import java.util.Objects;

public class ZamjenaID implements Serializable {
    private Long idLanca;
    private Short redniBroj;

    public ZamjenaID() {}

    public ZamjenaID(Long idLanca, Short redniBroj) {
        this.idLanca = idLanca;
        this.redniBroj = redniBroj;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZamjenaID zamjenaID = (ZamjenaID) o;
        return idLanca.equals(zamjenaID.idLanca) &&
                redniBroj.equals(zamjenaID.redniBroj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLanca, redniBroj);
    }
}
