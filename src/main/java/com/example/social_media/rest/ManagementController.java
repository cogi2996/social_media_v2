package com.example.social_media.rest;

import com.example.social_media.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @GetMapping
    public String get() {
        Authentication authentication = authenticationFacade.getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        return userDetails.getUsername();
    }
    @PostMapping
    public String post() {
        return "POST:: management controller";
    }
    @PutMapping
    public String put() {
        return "PUT:: management controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE:: management controller";
    }
}
