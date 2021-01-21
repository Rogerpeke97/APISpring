package com.apisnake.api.controller;

import com.apisnake.api.service.UserService;
import com.apisnake.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String checkLogin(@RequestBody Feedback feedback){
        System.out.println("feedback.getUsername");
        return userService.signin(feedback.getUsername(), feedback.getPassword());
        //return userDetailsImp.loadUserByUsername(feedback.getUsername());
    }
    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String register(@RequestBody Feedback feedback){
        System.out.println("signing up");
        return userService.signup(modelMapper.map(feedback, User.class));
        //return userDetailsImp.loadUserByUsername(feedback.getUsername());
    }

    @PostMapping(value = "/account", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String userData(@RequestBody JwtMap jwtMap){
        // THE FILTERING CHECKS FOR AUTHENTICATED REQUEST
        // LOOKS AT THE HEADER OF THE JWT FOR THE TOKEN 
        // IF IT PASSES THEN THIS EXECUTES
        return (userService.whoami(jwtMap.getAuthorization()) + "," + "you are authenticated!");
        //return userDetailsImp.loadUserByUsername(feedback.getUsername());
    }

    @PostMapping(value = "/accountdetails", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String userInfo(@RequestBody JwtMap jwtMap){
        // THE FILTERING CHECKS FOR AUTHENTICATED REQUEST
        // LOOKS AT THE HEADER OF THE JWT FOR THE TOKEN 
        // IF IT PASSES THEN THIS EXECUTES
        return userService.getUserData(jwtMap.getAuthorization());
        //return userDetailsImp.loadUserByUsername(feedback.getUsername());
    }

    @PostMapping(value = "/changePassword", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String changePassword(@RequestBody NewPassword newpassword){
        try{
            userService.changePassword(modelMapper.map(newpassword, User.class));
            return "Password succesfully changed!";
        }
        catch(Exception e){
            return "Incorrect username or password";
        }
    }
    @PostMapping(value = "/score", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String score(@RequestBody JwtMap jwtMap){
        try{   
            return userService.getScore(jwtMap.getAuthorization()).toString();
        }
        catch(Exception e){
            return "Invalid token/expired";
        }
    }
    @PostMapping(value = "/uploadscore", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String upload(@RequestBody JwtMap jwtMap){
        try{   
            return userService.uploadScore(jwtMap.getAuthorization(), jwtMap.getScore());
        }
        catch(Exception e){
            return "Invalid token/expired";
        }
    }
}
