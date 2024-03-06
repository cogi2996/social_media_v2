package com.example.social_media.security;

import com.example.social_media.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    UserDetails getUserDetails();

    Role getRole();
    User getUser();
}
