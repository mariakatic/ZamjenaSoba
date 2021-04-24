package com.piccologrupa.zamjenasoba.request;

import com.piccologrupa.zamjenasoba.domain.UserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PostEditRequest {

    private Long postID;

    private Long domID;

    private short paviljon;

    private short kat;

    private short kategorijaSobe;

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public Long getDomID() {
        return domID;
    }

    public void setDomID(Long domID) {
        this.domID = domID;
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
}