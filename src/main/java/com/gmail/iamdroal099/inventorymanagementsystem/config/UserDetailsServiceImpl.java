package com.gmail.iamdroal099.inventorymanagementsystem.config;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.User;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(()-> new UsernameNotFoundException
                        ("User with login " + username + " not found"));
        return UserDetailsImpl.build(user);
    }
}
