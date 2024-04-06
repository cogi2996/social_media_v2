package com.example.social_media.controller;

import com.example.social_media.DTO.AuthenticationRequest;
import com.example.social_media.dao.AccountRepository;
import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticaionController {
    private final AuthenticationService authenticationService;
    private final AccountRepository accountRepository;
    private final IAuthenticationFacade authenticationFacade;

    @GetMapping("/login")
    public String getIntoLogin(Model model) {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("testUser123@gmail.com");
        request.setPassword("123456");
        model.addAttribute("request", request);
        return "dashboard/sign-in";
    }


    @PostMapping("/getAuth")
    public String getAuth(@ModelAttribute AuthenticationRequest request, HttpSession session, @NonNull HttpServletRequest req, HttpServletResponse resp) {
        authenticationService.baseAuthenticate(request, req);
        Cookie cookie = new Cookie("access_token", authenticationService.authenticate(request).getAccessToken());
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        cookie.setPath("/");
        resp.addCookie(cookie);
        User user = accountRepository.findByEmail(request.getEmail()).get().getUser();
        session.setAttribute("user", user);
        User sessionUser = (User) session.getAttribute("user");
        return "redirect:/home/index";

    }

}