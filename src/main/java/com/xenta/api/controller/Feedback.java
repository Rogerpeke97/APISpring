package com.xenta.api.controller;

import java.io.Serializable;
import java.util.List;

import com.xenta.api.user.Role;

public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String newPassword;
    private String date;
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

    public String getNewPassword(){
        return newPassword;
    }

    public void setNewPassword(String newPassword){
        this.newPassword = newPassword;
    }

    public String getDate(){
        return date;
      }
    
      public void setDate(String date){
        this.date = date;
      }
    

    public List<Role> getRoles() {
        return roles;
      }
    
      public void setRoles(List<Role> roles) {
        this.roles = roles;
      }
}
