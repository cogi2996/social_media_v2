package com.example.social_media.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    UserDetails getUserDetails();

    Role getRole();
}
