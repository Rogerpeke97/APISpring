package com.apisnake.api.controller;

import com.apisnake.api.service.UserService;
import com.apisnake.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;


@Controller
public class ApiController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String checkLogin(@RequestBody Feedback feedback){
        return userService.signin(feedback.getUsername(), feedback.getPassword());
        //return userDetailsImp.loadUserByUsername(feedback.getUsername());
    }
    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String register(@RequestBody Feedback feedback){
        return userService.signup(modelMapper.map(feedback, User.class));
        //return userDetailsImp.loadUserByUsername(feedback.getUsername());
    }
    
    
}
