package com.example.social_media.controller;

import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/connection")
@RequiredArgsConstructor
public class ConnectionController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    @GetMapping
    public String getIntoIndex() {
        return "app/connection";
    }

}
