package com.xenta.api.user;

import java.sql.Date;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.JoinColumn;

import lombok.Data;
import lombok.Getter;
import lombok.AccessLevel;

@Entity
@Table(name = "users_table")
@Data public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id; // IM GENERATING A NEW ID THAT IS WHY WHEN I CHANGE PASSWORD
  // IT ACTS AS IF ITS A NEW USER BECAUSE THE ID's DO NOT MATCH, THEREFORE,
  // THE USERNAME IS ALREADY IN USE
  @Column(unique = true, nullable = false)
  private String username;

  private String password;
  
  @Column(unique = true, nullable = false)
  private String name;

  private Integer score = 0;

  private Date created_at;

  private Date updated_at;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinTable( 
      name = "users_roles", 
      joinColumns = @JoinColumn(
        name = "user_id", referencedColumnName = "id"), 
      inverseJoinColumns = @JoinColumn(
        name = "role_id", referencedColumnName = "id")
        ) 
  private Collection<Role> roles;

}
