package com.apisnake.api.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.apisnake.api.dao.UserDAO;
import com.apisnake.api.entities.Userentity;

import org.springframework.stereotype.Service;

@Service
public class DataFiller {
    private final UserDAO userDAO;
    public DataFiller(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    @PostConstruct
    @Transactional
    public void fillData(){
        Userentity ignacio = new Userentity("myjajksls@jasjdasj.com", "22225825");
        userDAO.save(ignacio);
    }
    
}
