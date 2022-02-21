package com.xenta.api.service;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.xenta.api.repositories.UsersRepository;
import com.xenta.api.security.JwtTokenProvider;
import com.xenta.api.service.Pojos.ResponseGeneric;
import com.xenta.api.user.Role;
import com.xenta.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONObject;

import static java.util.stream.Collectors.toList;

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

  public static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");

  public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("/^(?=.*[A-Z])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8,}$/");

  public static final Pattern VALID_NAME_REGEX = Pattern.compile("/^().{1,15}$/");

  private boolean areFieldsValid(String username, String password, String name) {
    Boolean isValidEmail = VALID_EMAIL_REGEX.matcher(username).find();
    Boolean isValidPassword = VALID_PASSWORD_REGEX.matcher(password).find();
    if(name.length() == 0){
      return isValidEmail && isValidPassword;
    }
    Boolean isValidName = VALID_NAME_REGEX.matcher(name).find();
    return isValidEmail && isValidPassword && isValidName;
  }

  private boolean authenticateUser(String username, String password) {
    try {
      if(!areFieldsValid(username, password, "")){
        throw new Error("Invalid username or password");
      }
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

  public ResponseGeneric<String> signIn(String username, String password) {
    if (authenticateUser(username, password)) {
      Collection<Role> roles = usersRepository.findByUsername(username).getRoles();
      String token = jwtTokenProvider.createToken(username, rolesToList(roles));
      ResponseGeneric<String> signInObject = new ResponseGeneric<>(token, "Successfully signed in", "");
      return signInObject;
    } else {
      ResponseGeneric<String> signInObject = new ResponseGeneric<>("", "", "Invalid username or password");
      return signInObject;
    }
  }
  
  public List<Role> rolesToList(Collection<Role> roles){
    return roles.stream().collect(toList());
  }

  public ResponseGeneric<String> signUp(User user) {
    try{
      if(!areFieldsValid(user.getUsername(), user.getPassword(), user.getName())){
        throw new Error("Invalid username or password");
      }
      if (usersRepository.existsByUsername(user.getUsername())) {
        ResponseGeneric<String> signUpObject = new ResponseGeneric<>("", "", "Username is already taken!");
        return signUpObject;
      }
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      usersRepository.save(user);
      ResponseGeneric<String> signUpObject = new ResponseGeneric<>("", "Successfully signed up", "");
      return signUpObject;
    } catch (Exception e) {
      ResponseGeneric<String> signUpObject = new ResponseGeneric<>("", "", "Invalid username or password");
      return signUpObject;
    }

  }

  public ResponseGeneric<String> changePassword(String newPassword, String token) {
    try {
      Boolean isValidPassword = VALID_PASSWORD_REGEX.matcher(newPassword).find();
      if(isValidPassword){
        throw new Error("Invalid password");
      }
      User userByToken = usersRepository.findByUsername(getUserFromToken(token));
      userByToken.setPassword(passwordEncoder.encode(newPassword));
      usersRepository.save(userByToken);
      ResponseGeneric<String> passwordUpdateObject = new ResponseGeneric<>("", "Successfully changed password", "");
      return passwordUpdateObject;
    } catch (AuthenticationException e) {
      ResponseGeneric<String> passwordUpdateObject = new ResponseGeneric<>("", "", "Invalid token");
      return passwordUpdateObject;
    }
  }

  public ResponseGeneric<String> updateScore(String token, Integer newScore) {
    User user = usersRepository.findByUsername(getUserFromToken(token));
    if (user.getScore() < newScore) {
      user.setScore(newScore);
      usersRepository.save(user);
      ResponseGeneric<String> scoreObject = new ResponseGeneric<>("", "Successfully updated score", "");
      return scoreObject;
    } 
    ResponseGeneric<String> scoreObject = new ResponseGeneric<>("", "", "Score must be greater than current score");
    return scoreObject;
  }

  public ResponseGeneric<JSONObject> getUserData(String token) {
    JSONObject userData = new JSONObject();
    try {
      User user = usersRepository.findByUsername(getUserFromToken(token));
      userData.put("username", user.getUsername());
      userData.put("score", user.getScore());
      userData.put("roles", user.getRoles());
      userData.put("id", user.getId());
      userData.put("name", user.getName());
      userData.put("created_at", user.getCreated_at());
      userData.put("updated_at", user.getUpdated_at());
      ResponseGeneric<JSONObject> userObject = new ResponseGeneric<>(userData, "Successfully retrieved user data", "");
      return userObject;
    } catch (AuthenticationException e) {
      ResponseGeneric<JSONObject> userObject = new ResponseGeneric<>(null, "", "Invalid token");
      return userObject;
    }
  }

  public ResponseGeneric<String> refresh(String token) {
    try {
      String username = getUserFromToken(token);
      Collection<Role> roles = usersRepository.findByUsername(username).getRoles();
      String newToken = jwtTokenProvider.createToken(username, rolesToList(roles));
      ResponseGeneric<String> tokenObject = new ResponseGeneric<>(newToken, "Successfully refreshed token", "");
      return tokenObject;
    } catch (AuthenticationException e) {
      ResponseGeneric<String> tokenObject = new ResponseGeneric<>("", "", "Invalid token");
      return tokenObject;
    }
  }

}
