package com.example.social_media.service;

import com.example.social_media.DTO.AuthenticationRequest;
import com.example.social_media.DTO.AuthenticationResponse;
import com.example.social_media.dao.AccountRepository;
import com.example.social_media.dao.TokenRepository;
import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.Account;
import com.example.social_media.entity.Token;
import com.example.social_media.entity.TokenType;
import com.example.social_media.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    public AuthenticationResponse register(Account theAccount , User newUser  ) {
        var account = Account.builder()
                .email(theAccount.getEmail())
                .password(passwordEncoder.encode(theAccount.getPassword())) // bởi vì encrypt nên giờ phải decode
                .role(theAccount.getRole())
                .build();
        newUser.setAccount(account);
        userRepository.save(newUser);
        var jwtToken = jwtService.generateToken(account);
        // save token into account
        saveAccountToken(account, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void saveAccountToken(Account account, String jwtToken) {
        Token token = Token.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    // phương thức này dược định nghĩa đúng sai bởi authenticationManager.authenticationProvider
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var account = accountRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        // revoke all user's token
        revokeAllAccountTokens(account);
        // save new token
        saveAccountToken(account, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void revokeAllAccountTokens(Account account) {
        //all user's token
        var validUserTokens = tokenRepository.findAllValidTokensByAccount(account.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        // revoked all before save new token
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        // save new token
        tokenRepository.saveAll(validUserTokens);
    }
}
