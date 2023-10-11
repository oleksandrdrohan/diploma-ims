package com.gmail.iamdroal099.inventorymanagementsystem.repo;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByLogin(String login);
}
