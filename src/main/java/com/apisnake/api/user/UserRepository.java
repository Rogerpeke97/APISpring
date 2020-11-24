package com.apisnake.api.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query(value = "SELECT * FROM userentity WHERE user_entity = ?1", nativeQuery = true)
    public User getUserName(String user_entity);
    
}
