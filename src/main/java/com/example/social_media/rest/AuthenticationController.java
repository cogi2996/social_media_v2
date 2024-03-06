package com.example.social_media.rest;

import com.example.social_media.DTO.AccountDTO;
import com.example.social_media.DTO.AuthenticationRequest;
import com.example.social_media.DTO.AuthenticationResponse;
import com.example.social_media.DTO.UserDTO;
import com.example.social_media.entity.Account;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.security.Role;
import com.example.social_media.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final IAuthenticationFacade authenticationFacade;
    private final ModelMapper modelMapper;
    //
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AccountDTO accountDTO){
        System.out.println(accountDTO);
        Account newAccount = convertToAccountEntity(accountDTO);
        User newUser = convertToUserEntity(accountDTO.getUserDTO());
        // không phải admin thì new user là user hết
        if(!(authenticationFacade.getRole() == Role.ADMIN)){
            newAccount.setRole(Role.USER);
        }
        AuthenticationResponse token =  authenticationService.register(newAccount,newUser);
        return  ResponseEntity.ok(token );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

    private Account convertToAccountEntity(AccountDTO accountDTO){
        return modelMapper.map(accountDTO,Account.class);
    }

    private User convertToUserEntity(UserDTO userDTO){
        return modelMapper.map(userDTO,User.class);
    }


}
