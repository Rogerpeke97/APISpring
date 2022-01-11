package com.xenta.api.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userentity")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id; // IM GENERATING A NEW ID THAT IS WHY WHEN I CHANGE PASSWORD
  // IT ACTS AS IF ITS A NEW USER BECAUSE THE ID's DO NOT MATCH, THEREFORE,
  // THE USERNAME IS ALREADY IN USE
  @Column(unique = true, nullable = false)
  private String username;

  private String password;

  private String newPassword;

  private Integer score = 0;

  private String avatarColor;

  private String date;

  @ElementCollection(fetch = FetchType.EAGER)
  List<Role> roles;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public String getAvatarColor() {
    return avatarColor;
  }

  public void setAvatarColor(String color) {
    this.avatarColor = color;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public String getUserInfo() {
    String info = this.getUsername() + "," + this.getDate() + "," + this.getScore().toString();
    return info;
  }
}
