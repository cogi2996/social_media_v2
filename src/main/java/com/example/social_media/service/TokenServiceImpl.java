package com.example.social_media.service;

import com.example.social_media.dao.TokenRepository;
import com.example.social_media.entity.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    private final TokenRepository tokenRepository;
    @Override
    public List<Token> findAllValidTokensByAccount(Integer accountId) {
        return tokenRepository.findAllValidTokensByAccount(accountId);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
