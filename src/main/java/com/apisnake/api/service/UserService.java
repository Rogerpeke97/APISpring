package com.apisnake.api.service;

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
        userRepository.save(user);
        return "User succesfully created";
      } else {
        throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
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
