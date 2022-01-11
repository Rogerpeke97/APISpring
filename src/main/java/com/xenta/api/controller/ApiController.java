package com.xenta.api.controller;

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

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {
  @Autowired
  private UserService userService;

  @GetMapping(value = "/user", produces = "application/json")
  @ResponseBody
  public String answerExampleRequest() {
    return "Hello World";
  }

  @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject signIn(@RequestBody User user) {
    return userService.signIn(user);
  }

  @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject register(@RequestBody User user) {
    System.out.printf("signing up", user);
    return userService.signUp(user);
  }

  @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject userInfo(@RequestHeader("Authorization") String token) {
    return userService.getUserData(token);
  }

  @PostMapping(value = "/changepassword", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public JSONObject changePassword(@RequestBody NewPassword newpassword, @RequestHeader("Authorization") String token) {
    return userService.changePassword(newpassword.getNewPassword(), token);
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
}
