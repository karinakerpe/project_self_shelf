package com.selfshelf.project.service;

import com.selfshelf.project.exception.UserNotFoundException;
import com.selfshelf.project.model.DAO.UserEntityDao;
import com.selfshelf.project.model.UserEntity;
import com.selfshelf.project.model.UserRole;
import com.selfshelf.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserEntity saveUser(UserEntityDao input) {
        UserEntity user = new UserEntity(input.getId(), input.getEmail(), input.getPassword(),input.getFirstName(),
                input.getLastName(), input.getUserRole(),input.getReservations(),input.getIssuedBooks());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setUserRole(UserRole.ADMIN);
        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }


    public UserEntity getById (Long id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("User not found!")
        );
    }

    public UserEntity update(Long id, UserEntity user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(user.getEmail());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        existingUser.setPassword(encodedPassword);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setUserRole(user.getUserRole());

        return userRepository.save(existingUser);
    }

}
