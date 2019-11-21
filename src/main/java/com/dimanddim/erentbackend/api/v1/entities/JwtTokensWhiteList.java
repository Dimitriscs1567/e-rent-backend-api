package com.dimanddim.erentbackend.api.v1.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jwt_tokens_white_list")
public class JwtTokensWhiteList{

    @Id
    @Column(name = "token", unique = true)
    private String token;

    public JwtTokensWhiteList() {
    }

    public JwtTokensWhiteList(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}