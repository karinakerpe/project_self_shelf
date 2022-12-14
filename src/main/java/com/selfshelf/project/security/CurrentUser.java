package com.selfshelf.project.security;


import com.selfshelf.project.model.UserEntity;
import com.selfshelf.project.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUser {
    @Autowired
    UserServiceImpl userService;
    public long getCurrentUserId() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = details.getUsername();
        long userId = -1;
        for (UserEntity user : userService.getUsers()) {
            if (user.getEmail().equals(username)) {
                userId = user.getId();
                break;
            }
        }
        return userId;
    }

    public UserEntity getCurrentUser() {
        return userService.getById(getCurrentUserId());
    }
}
