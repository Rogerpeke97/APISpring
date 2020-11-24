package com.apisnake.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsImp implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String user_entity) throws Error {
        User user = userRepository.getUserName(user_entity);
        if (user == null) {
            throw new Error("User Not Found!");
        }
        return new TheUserDetails(user);
    }

    
}
