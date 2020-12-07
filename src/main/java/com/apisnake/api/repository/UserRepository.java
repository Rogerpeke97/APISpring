package com.apisnake.api.repository;

import javax.transaction.Transactional;

import com.apisnake.api.user.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    
  boolean existsByUsername(String username);

  User findByUsername(String username);

  @Transactional
  void deleteByUsername(String username);
    
}
