package com.gmail.iamdroal099.inventorymanagementsystem.service;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.User;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Transactional
    public void saveUser(User user){
        user.setPassword(encoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean isUserExists(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user.isPresent();
    }
}
