package com.example.social_media.service;

import com.example.social_media.DTO.AuthenticationRequest;
import com.example.social_media.DTO.AuthenticationResponse;
import com.example.social_media.DTO.RegisterRequest;
import com.example.social_media.dao.AccountRepository;
import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.Account;
import com.example.social_media.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public AuthenticationResponse register(RegisterRequest request , User newUser  ) {
        var account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // bởi vì encrypt nên giờ phải decode
                .role(request.getRole())
                .build();
        newUser.setAccount(account);
        userRepository.save(newUser);
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
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
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}
