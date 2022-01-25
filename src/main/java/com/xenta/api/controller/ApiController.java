package com.xenta.api.controller;

import javax.servlet.http.HttpServletResponse;

import com.xenta.api.service.UserService;
import com.xenta.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ResponseHeader;
import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {
  @Autowired
  private UserService userService;

  @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
  public JSONObject signIn(@RequestBody User user, HttpServletResponse response) {
    JSONObject signInResult = userService.signIn(user.getUsername(), user.getPassword());
    if (signInResult.get("token") != null) {
      System.out.println(signInResult.get("token"));
      response.addHeader("Authorization", signInResult.get("token").toString());
      response.setHeader("Access-Control-Expose-Headers", "Content-Type, Accept, Authorization");
    }
    return signInResult;
  }

  @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject register(@RequestBody User user) {
    return userService.signUp(user);
  }

  @GetMapping(value = "/user", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject userInfo(@RequestHeader("Authorization") String token) {
    
    return userService.getUserData(token);

  }

  @PostMapping(value = "/changepassword", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject changePassword(@RequestBody ChangePasswordForm form, @RequestHeader("Authorization") String token) {
    JSONObject changePasswordResult = userService.signIn(form.getUsername(), form.getOldPassword());
    if(changePasswordResult.get("token") != null) {
      return userService.changePassword(form.getUsername(), form.getNewPassword());
    }
    return changePasswordResult;
  }

  @PostMapping(value = "/score", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject score(@RequestHeader("Authorization") String token) {
    return userService.getScore(token);
  }

  @PostMapping(value = "/updatescore", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject upload(@RequestBody Integer score, @RequestHeader("Authorization") String token) {
    return userService.updateScore(token, score);
  }

  @GetMapping(value = "/ping", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject ping(@RequestHeader("Authorization") String token, HttpServletResponse response) {
    JSONObject refreshTokenOk = userService.refresh(token);
    if (refreshTokenOk.get("token") != null) {
      response.addHeader("Authorization", refreshTokenOk.get("token").toString());
      response.setHeader("Access-Control-Expose-Headers", "Content-Type, Accept, Authorization");
    }
    return refreshTokenOk;
  }
}
