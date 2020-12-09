package com.apisnake.api.security;

import com.apisnake.api.repository.*;
import com.apisnake.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsImp implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String user_entity) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(user_entity);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + user_entity + "' not found");
        }
        return org.springframework.security.core.userdetails.User
        .withUsername(user_entity)
        .password(user.getPassword())
        .authorities(user.getRoles())
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
    }

    
}
