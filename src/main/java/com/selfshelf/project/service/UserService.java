package com.selfshelf.project.service;

import com.selfshelf.project.model.DAO.UserEntityDao;
import com.selfshelf.project.model.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntityDao user);
    UserEntity getUserByEmail(String email);
    List<UserEntity> getUsers();
}
