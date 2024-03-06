package com.example.social_media.security;

import com.example.social_media.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade implements IAuthenticationFacade {
    private UserDetailsService userDetailsService;

    @Override
    public Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserDetails getUserDetails() {
        System.out.println("start here");
        System.out.println(this.getAuthentication());
        System.out.println("end here");
        Authentication authentication = getAuthentication();
        // không authen hoặc authen nhưng là người dùng ẩn danh ( chưa có trong db )
        if (this.getAuthentication() == null ||authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (UserDetails) this.getAuthentication().getPrincipal();
    }


    @Override
    public Role getRole() {
        if (this.getUserDetails() == null) {
            return null;
        }
        Account account = (Account) this.getUserDetails();
        return account.getRole();
    }


}
