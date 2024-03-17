package com.example.social_media.service;

import com.example.social_media.entity.Token;

import java.util.List;
import java.util.Optional;

public interface TokenService {
    List<Token> findAllValidTokensByAccount(Integer accountId);
    Optional<Token> findByToken(String token);
}
