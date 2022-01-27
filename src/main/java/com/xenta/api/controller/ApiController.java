package com.xenta.api.controller;

import javax.servlet.http.HttpServletResponse;

import com.xenta.api.service.UserService;
import com.xenta.api.service.Pojos.ResponseGeneric;
import com.xenta.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {
  @Autowired
  private UserService userService;

  @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
  public ResponseGeneric<String> signIn(@RequestBody User user, HttpServletResponse response) {
    ResponseGeneric<String> signedIn = userService.signIn(user.getUsername(), user.getPassword());
    if (signedIn.getData() == "") {
      return signedIn;
    }
    response.addHeader("Authorization", signedIn.getData());
    response.setHeader("Access-Control-Expose-Headers", "Content-Type, Accept, Authorization");
    signedIn.setData("");
    return signedIn;
  }

  @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseGeneric<String> register(@RequestBody User user) {
    return userService.signUp(user);
  }

  @GetMapping(value = "/api/user", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseGeneric<JSONObject> userInfo(@RequestHeader("Authorization") String token) {
    return userService.getUserData(token);
  }

  @PostMapping(value = "/api/changepassword", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseGeneric<String> changePassword(@RequestBody ChangePasswordForm form, @RequestHeader("Authorization") String token) {
    return userService.changePassword(form.getUsername(), form.getNewPassword());
  }

  @PostMapping(value = "/api/updatescore", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseGeneric<String> upload(@RequestBody Integer score, @RequestHeader("Authorization") String token) {
    return userService.updateScore(token, score);
  }

  @GetMapping(value = "/api/ping", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseGeneric<String> ping(@RequestHeader("Authorization") String token, HttpServletResponse response) {
    ResponseGeneric<String> refreshedToken = userService.refresh(token);
    response.addHeader("Authorization", refreshedToken.getData());
    response.setHeader("Access-Control-Expose-Headers", "Content-Type, Accept, Authorization");
    refreshedToken.setData("");
    return refreshedToken;
  }
}
