package com.apisnake.api.dao;

import java.util.UUID;

import com.apisnake.api.entities.Userentity;

import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<Userentity, UUID>{}