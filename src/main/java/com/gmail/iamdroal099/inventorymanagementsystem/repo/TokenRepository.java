package com.gmail.iamdroal099.inventorymanagementsystem.repo;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByTokenName(String tokenName);
}
