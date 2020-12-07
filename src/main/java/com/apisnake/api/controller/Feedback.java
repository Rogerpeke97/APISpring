package com.apisnake.api.controller;

import java.io.Serializable;
import java.util.List;

import com.apisnake.api.user.Role;

public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    List<Role> roles;

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
      }
    
      public void setRoles(List<Role> roles) {
        this.roles = roles;
      }
}
