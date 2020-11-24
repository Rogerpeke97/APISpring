package com.apisnake.api.controller;

import com.apisnake.api.user.UserDetailsImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {
    @Autowired
    private UserDetailsImp userDetailsImp;

    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public UserDetails checkLogin(@RequestBody Feedback feedback){
        return userDetailsImp.loadUserByUsername(feedback.getUsername());
    }
    
}
