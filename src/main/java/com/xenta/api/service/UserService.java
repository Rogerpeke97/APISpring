package com.xenta.api.service;

import java.util.Arrays;

import com.xenta.api.exception.CustomException;
import com.xenta.api.security.JwtTokenProvider;
import com.xenta.api.user.User;
import com.xenta.api.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
      return authentication.isAuthenticated();
    } catch (AuthenticationException e) {
      return false;
    }
  }

  private String getUserFromToken(String token) {
    return jwtTokenProvider.getUsername(token);
  }

  public JSONObject signIn(User user) {
    String username = user.getUsername();
    String password = user.getPassword();
    JSONObject response = new JSONObject();
    if (authenticateUser(username, password)) {
      String token = jwtTokenProvider.createToken(username, usersRepository.findByUsername(username).getRoles());
      response.put("username", username);
      response.put("token", token);
    } else {
      response.put("error", "Invalid username or password");
    }
    return response;
  }

  public JSONObject signUp(User user) {
    String username = user.getUsername();
    String password = user.getPassword();
    JSONObject response = new JSONObject();
    if (usersRepository.existsByUsername(username)) {
      response.put("error", "Username is already taken");
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

  public JSONObject changeAvatarColor(String token, String color) {
    User user = usersRepository.findByUsername(getUserFromToken(token));
    JSONObject response = new JSONObject();
    String[] colors = { "red", "blue", "green", "yellow", "black", "white" };
    if (Arrays.asList(colors).contains(color)) {
      user.setAvatarColor(color);
      usersRepository.save(user);
      response.put("message", "Avatar color succesfully changed!");
    } else {
      response.put("error", "Color not supported");
    }
    return response;
  }

  public JSONObject getUserData(String token) {
    JSONObject response = new JSONObject();
    try {
      User user = usersRepository.findByUsername(getUserFromToken(token));
      response.put("user", user);
    } catch (AuthenticationException e) {
      response.put("error", "Token is invalid or expired");
    }
    return response;
  }

  public JSONObject refresh(String token) {
    JSONObject response = new JSONObject();
    try {
      String username = getUserFromToken(token);
      String newToken = jwtTokenProvider.createToken(username, usersRepository.findByUsername(username).getRoles());
      response.put("token", newToken);
    } catch (AuthenticationException e) {
      response.put("error", "Token is invalid or expired");
    }
    return response;
  }

}
