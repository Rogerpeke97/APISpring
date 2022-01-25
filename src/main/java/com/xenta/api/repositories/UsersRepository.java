package com.xenta.api.repositories;

import javax.transaction.Transactional;

import com.xenta.api.user.User;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {

    
  boolean existsByUsername(String username);

  User findByUsername(String username);

  @Transactional
  void deleteByUsername(String username);
    
}
