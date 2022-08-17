package com.selfshelf.project.service;

import com.selfshelf.project.model.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    UserEntity getUserByEmail(String email);
    List<UserEntity> getUsers();
}
