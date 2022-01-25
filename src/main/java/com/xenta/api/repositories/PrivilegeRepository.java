package com.xenta.api.repositories;
import com.xenta.api.user.Privilege;

import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

    
  Privilege findByName(String name);
    
}
