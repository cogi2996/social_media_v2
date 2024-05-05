package com.example.social_media.service;

import com.example.social_media.DTO.AuthenticationRequest;
import com.example.social_media.DTO.ChangePassRequestDTO;
import com.example.social_media.dao.AccountRepository;
import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.Account;
import com.example.social_media.entity.User;
import com.example.social_media.security.AuthenticationFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationFacade authenticationFacade;

    public void register(Account theAccount, User newUser) {
        var account = Account.builder()
                .email(theAccount.getEmail())
                .password(passwordEncoder.encode(theAccount.getPassword())) // bởi vì encrypt nên giờ phải decode
                .role(theAccount.getRole())
                .build();
        newUser.setAccount(account);
        userRepository.save(newUser);

    }

//    private void saveAccountToken(Account account, String jwtToken) {
//        Token token = Token.builder()
//                .account(account)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
//    }

    // phương thức này dược định nghĩa đúng sai bởi authenticationManager.authenticationProvider
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        // bản gốc
////        authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(
////                        request.getEmail(),
////                        request.getPassword()
////                )
////        );
//        // cẩn thận dùng bản này
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        var account = accountRepository.findByEmail(request.getEmail()).orElseThrow();
//        var jwtToken = jwtService.generateToken(account);
//        // revoke all user's token
//        revokeAllAccountTokens(account);
//        // save new token
//        saveAccountToken(account, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .build();
//    }

    public void baseAuthenticate(AuthenticationRequest request, HttpServletRequest req) {
        // Tạo đối tượng AuthenticationToken với tên người dùng và mật khẩu
        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        // Lấy SecurityContext hiện tại
        SecurityContext sc = SecurityContextHolder.getContext();
        // Đặt Authentication vào SecurityContext
        sc.setAuthentication(auth);
        // Lấy hoặc tạo HttpSession
        HttpSession session = req.getSession(true);
        session.setMaxInactiveInterval( 60*60*24); // 1 day
        // Lưu SecurityContext vào HttpSession
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
    }


//    private void revokeAllAccountTokens(Account account) {
//        //all user's token
//        var validUserTokens = tokenRepository.findAllValidTokensByAccount(account.getId());
//        if (validUserTokens.isEmpty()) {
//            return;
//        }
//        // revoked all before save new token
//        validUserTokens.forEach(token -> {
//            token.setRevoked(true);
//            token.setExpired(true);
//        });
//        // save new token
//        tokenRepository.saveAll(validUserTokens);
//    }

    public void changePassword(ChangePassRequestDTO changePassRequestDTO) {
        String email = authenticationFacade.getAuthentication().getName();
        String oldPass = changePassRequestDTO.getOldPassword();
        String newPassword = changePassRequestDTO.getNewPassword();
        // auth
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,
                oldPass);
        Authentication auth = authenticationManager.authenticate(token);
        // save new entity account
        Account account = accountRepository.findByEmail(email).orElseThrow();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }
}
