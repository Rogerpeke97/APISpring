package com.xenta.api.service;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;

import com.xenta.api.repositories.UsersRepository;
import com.xenta.api.security.JwtTokenProvider;
import com.xenta.api.user.Role;
import com.xenta.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;


import net.minidev.json.JSONObject;

@Service
public class UserService {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  private boolean authenticateUser(String username, String password) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              username,
              password));
      System.out.println(authentication);
      return authentication.isAuthenticated();
    } catch (AuthenticationException e) {
      System.out.println(e);
      return false;
    }
  }

  private String getUserFromToken(String token) {
    return jwtTokenProvider.getUsername(token);
  }

  public JSONObject signIn(String username, String password) {
    JSONObject response = new JSONObject();
    System.out.println("IS IT FAILING? " + authenticateUser(username, password));
    if (authenticateUser(username, password)) {
      Collection<Role> roles = usersRepository.findByUsername(username).getRoles();
      String token = jwtTokenProvider.createToken(username, rolesToList(roles));
      response.put("message", "Successfully signed in");
      response.put("token", token);
    } else {
      response.put("error", "Invalid username or password");
    }
    return response;
  }
  
  public List<Role> rolesToList(Collection<Role> roles){
    return roles.stream().collect(toList());
  }

  public JSONObject signUp(User user) {
    String username = user.getUsername();
    String password = user.getPassword();
    System.out.println(user);
    JSONObject response = new JSONObject();
    if (usersRepository.existsByUsername(username)) {
      response.put("error", "Email is already taken");
    } else {
      user.setPassword(passwordEncoder.encode(password));
      usersRepository.save(user);
      response.put("message", "User registered successfully");
    }
    return response;
  }

  public JSONObject changePassword(String newPassword, String token) {
    JSONObject response = new JSONObject();
    try {
      User userByToken = usersRepository.findByUsername(getUserFromToken(token));
      userByToken.setPassword(passwordEncoder.encode(newPassword));
      usersRepository.save(userByToken);
      response.put("message", "Password succesfully changed!");
    } catch (AuthenticationException e) {
      response.put("error", "Token is invalid or expired");
    }
    return response;
  }

  public JSONObject getScore(String token) {
    JSONObject response = new JSONObject();
    try {
      Integer score = usersRepository.findByUsername(getUserFromToken(token)).getScore();
      response.put("score", score);
    } catch (AuthenticationException e) {
      response.put("error", "Token is invalid or expired");
    }
    return response;
  }

  public JSONObject updateScore(String token, Integer newScore) {
    User user = usersRepository.findByUsername(getUserFromToken(token));
    JSONObject response = new JSONObject();
    if (user.getScore() < newScore) {
      user.setScore(newScore);
      usersRepository.save(user);
      response.put("message", "New highscore updated!");
    } else {
      response.put("message", "New highscore is not higher than current highscore");
    }
    return response;
  }

  public JSONObject getUserData(String token) {
    JSONObject response = new JSONObject();
    try {
      User user = usersRepository.findByUsername(getUserFromToken(token));
      response.put("username", user.getUsername());
      response.put("score", user.getScore());
      response.put("created_at", user.getCreated_at());
      response.put("updated_at", user.getUpdated_at());
      response.put("name", user.getName());
      response.put("id", user.getId());
    } catch (AuthenticationException e) {
      response.put("error", "Token is invalid or expired");
    }
    return response;
  }

  public JSONObject refresh(String token) {
    JSONObject response = new JSONObject();
    try {
      String username = getUserFromToken(token);
      Collection<Role> roles = usersRepository.findByUsername(username).getRoles();
      String newToken = jwtTokenProvider.createToken(username, rolesToList(roles));
      response.put("token", newToken);
      response.put("message", "Token refreshed");
    } catch (AuthenticationException e) {
      response.put("error", "Token is invalid or expired");
    }
    return response;
  }

}
