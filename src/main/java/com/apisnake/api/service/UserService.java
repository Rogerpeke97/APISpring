package com.apisnake.api.service;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.apisnake.api.exception.CustomException;
import com.apisnake.api.security.JwtTokenProvider;
import com.apisnake.api.user.User;
import com.apisnake.api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  public String signin(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      return (username + "/n" + jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles()));
  } catch (AuthenticationException e) {
      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
  }
}

public String signup(User user) {
    if (!userRepository.existsByUsername(user.getUsername())) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Random generator = new Random();
        user.setScore(generator.nextInt(10));
        user.setDate(user.getDate());
        userRepository.save(user);
        return "User succesfully created";
      } else {
        throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
      }
}

public String changePassword(User user){
  try {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    user.setPassword(passwordEncoder.encode(user.getNewPassword()));
    user.setNewPassword(null);
    user.setId(userRepository.findByUsername(user.getUsername()).getId());
    user.setScore(userRepository.findByUsername(user.getUsername()).getScore());
    user.setAvatar(userRepository.findByUsername(user.getUsername()).getAvatar());
    user.setDate(user.getDate());
    userRepository.save(user);
    return "Password succesfully changed!";
} catch (AuthenticationException e) {
    throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
}
}

public Integer getScore(String token){
  try {
    return userRepository.findByUsername(jwtTokenProvider.getUsername(token)).getScore();
} catch (AuthenticationException e) {
    throw new CustomException("Invalid token/expired", HttpStatus.UNPROCESSABLE_ENTITY);
}
}


public String uploadScore(String token, Integer newScore){
  if(userRepository.findByUsername(jwtTokenProvider.getUsername(token)).getScore() < newScore){
  try {
     userRepository.findByUsername(jwtTokenProvider.getUsername(token)).setScore(newScore);
     return "Highest score updated!";
} catch (AuthenticationException e) {
    throw new CustomException("Error updating scores", HttpStatus.UNPROCESSABLE_ENTITY);
}
  }
  else{
    return "No new high scores";
  }
}


public String changeAvatarColor(String token, String newAvatar){
  try {
    userRepository.findByUsername(jwtTokenProvider.getUsername(token)).setAvatar(newAvatar);
    return "Avatar color updated!";
} catch (AuthenticationException e) {
   throw new CustomException("Error updating avatar", HttpStatus.UNPROCESSABLE_ENTITY);
}
}


public String getUserData(String token){
  try {
    return userRepository.findByUsername(jwtTokenProvider.getUsername(token)).getUserInfo();
} catch (AuthenticationException e) {
   throw new CustomException("Error getting user information", HttpStatus.UNPROCESSABLE_ENTITY);
}
}


public User search(String username) {
  User user = userRepository.findByUsername(username);
  if (user == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
  }
  return user;
}

public String whoami(String token) {
  return jwtTokenProvider.getUsername(token);
}

public String refresh(String username) {
  return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
  }

public boolean validateJwt(String jwt){
  return jwtTokenProvider.validateToken(jwt);
}

}
