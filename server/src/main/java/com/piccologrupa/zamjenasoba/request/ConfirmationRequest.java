package com.piccologrupa.zamjenasoba.request;

import javax.validation.constraints.NotNull;

public class ConfirmationRequest {

    @NotNull
    private Long idLanca;

    public Long getIdLanca() {
        return idLanca;
    }

    public void setIdLanca(Long idLanca) {
        this.idLanca = idLanca;
    }

}
