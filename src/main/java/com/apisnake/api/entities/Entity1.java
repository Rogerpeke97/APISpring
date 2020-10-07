package com.apisnake.api.entities;

import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

@MappedSuperclass
public class Entity1 {
    @Id @Type(type = "pg-uuid")
    private UUID id;

    public Entity1(){
        this.id = UUID.randomUUID();
    }
    
}
