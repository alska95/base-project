package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserRepository  {
    void save(UserEntity userEntity);
    Optional<UserEntity> findByUserId(String userId);
    List<UserEntity> findAll();
    Optional<UserEntity> findByEmail(String email);
    //crudRepository 사용해도 되지만 그냥 놔둠

}
