package com.example.social_media.rest;

import com.example.social_media.DTO.*;
import com.example.social_media.entity.Account;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final IAuthenticationFacade authenticationFacade;
    private final ModelMapper modelMapper;
    //
//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody AccountDTO accountDTO){
//        System.out.println(accountDTO);
//        Account newAccount = convertToAccountEntity(accountDTO);
//        User newUser = convertToUserEntity(accountDTO.getUserDTO());
//        // không phải admin thì new user là user hết
//        if(!(authenticationFacade.getRole() == Role.ADMIN)){
//            newAccount.setRole(Role.USER);
//        }
//        AuthenticationResponse token =  authenticationService.register(newAccount,newUser);
//        return  ResponseEntity.ok(token );
//    }



    @PostMapping("/change-password")
    public ResponseEntity<ModelMap> changePassword(@Validated @RequestBody ChangePassRequestDTO changePassRequestDTO, BindingResult bindingResult){
        //xử lí quăng lỗi nếu có lỗi
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            //return error change pass cho client
            ModelMap errorModelMap = new ModelMap();
            errorModelMap.addAttribute("message", errorMsg);
            System.out.println("biloi");
            System.out.println(errorMsg);
            return ResponseEntity.badRequest().body(errorModelMap);
        }
        //xử lí quăng lỗi nếu  mật khẩu cũ và mới giống nhau
        authenticationService.changePassword(changePassRequestDTO);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("message","Password changed successfully");
        return ResponseEntity.ok(modelMap);
    }


    private Account convertToAccountEntity(AccountDTO accountDTO){
        return modelMapper.map(accountDTO,Account.class);
    }

    private User convertToUserEntity(UserDTO userDTO){
        return modelMapper.map(userDTO,User.class);
    }


}
