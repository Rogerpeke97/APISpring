package com.xenta.api.controller;

import java.io.Serializable;

public class JwtMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private String authorization;
    private Integer score;
    public String getAuthorization(){
        return authorization;
    }
    public void setAuthorization(String authorization){
        this.authorization = authorization;
    }
    public Integer getScore(){
        return score;
    }
    public void setScore(Integer score){
        this.score = score;
    }
}
