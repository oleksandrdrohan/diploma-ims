package com.gmail.iamdroal099.inventorymanagementsystem.service;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Token;
import com.gmail.iamdroal099.inventorymanagementsystem.repo.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Transactional(readOnly = true)
    public Token getByTokenName(String tokenName){
        return tokenRepository.findByTokenName(tokenName);
    }

    @Transactional(readOnly = true)
    public boolean isValidToken(String tokenName) {
        Token token = getByTokenName(tokenName);

        long currentTimeMillis = System.currentTimeMillis();
        long tokenTimestamp = token.getCreationDate().getTime();

        long validityPeriodMillis = 10 * 60 * 1000;

        return currentTimeMillis - tokenTimestamp <= validityPeriodMillis;
    }

    @Transactional
    public void save(Token token) {
        tokenRepository.save(token);
    }

    @Transactional
    public void delete(Token token) {
        tokenRepository.delete(token);
    }
}
