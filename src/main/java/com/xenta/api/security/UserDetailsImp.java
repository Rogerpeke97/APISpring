package com.xenta.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xenta.api.repositories.*;
import com.xenta.api.user.Privilege;
import com.xenta.api.user.Role;
import com.xenta.api.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImp implements UserDetailsService {
    @Autowired
    private UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String user_entity) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(user_entity);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + user_entity + "' not found");
        }

        Collection<Role> userRoles = user.getRoles();

        Collection<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(getPrivileges(userRoles));

        return org.springframework.security.core.userdetails.User
        .withUsername(user_entity)
        .password(user.getPassword())
        .authorities(grantedAuthorities)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
    }


    private List<String> getPrivileges(Collection<Role> roles) {
 
      List<String> privileges = new ArrayList<>();
      List<Privilege> collection = new ArrayList<>();
      for (Role role : roles) {
          privileges.add(role.getName());
          collection.addAll(role.getPrivileges());
      }
      for (Privilege item : collection) {
          privileges.add(item.getName());
      }
      return privileges;
  }

  private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
      List<GrantedAuthority> authorities = new ArrayList<>();
      for (String privilege : privileges) {
          authorities.add(new SimpleGrantedAuthority(privilege));
      }
      return authorities;
  }
    
}
