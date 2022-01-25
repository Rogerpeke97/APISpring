package com.xenta.api.repositories;
import com.xenta.api.user.Role;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    
  Role findByName(String name);

    
}
