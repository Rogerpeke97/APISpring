package com.apisnake.api.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String user_entity;
    private String password;
    public String getUserName() {
        return user_entity;
    }
    public String getEncrytedPassword() {
        return password;
    }

}
