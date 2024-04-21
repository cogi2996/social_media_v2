package com.example.social_media.controller;

import com.example.social_media.DTO.AuthenticationRequest;
import com.example.social_media.Utils.OTPGenerator;
import com.example.social_media.dao.AccountRepository;
import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.Account;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.security.Role;
import com.example.social_media.service.AuthenticationService;
import com.example.social_media.service.EmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticaionController {
    private final AuthenticationService authenticationService;
    private final AccountRepository accountRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final EmailService emailService;

    @GetMapping("/login")
    public String getIntoLogin(Model model) {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("testUser123@gmail.com");
        request.setPassword("123456");
        model.addAttribute("request", request);
        return "web/sign-in";
    }


    @PostMapping("/getAuth")
    public String getAuth(@ModelAttribute AuthenticationRequest request, HttpSession session, @NonNull HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("here1");
        authenticationService.baseAuthenticate(request, req);
        System.out.println("here2");
        Cookie cookie = new Cookie("access_token", authenticationService.authenticate(request).getAccessToken());
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        cookie.setPath("/");
        resp.addCookie(cookie);
        System.out.println("here");
        User user = accountRepository.findByEmail(request.getEmail()).get().getUser();
        session.setAttribute("user", user);
        User sessionUser = (User) session.getAttribute("user");
        return "redirect:/home/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie = new Cookie("access_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String getIntoRegister(Model model) {
        User user = new User();
        model.addAttribute("newUser", user);
        return "web/register";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute User newUser, HttpSession session, @Param("password") String password, @Param("email") String email,Model model) {
        System.out.println("TAGUSER");
        System.out.println(password);
        System.out.println(email);
        System.out.println(newUser);
        String otp = OTPGenerator.generateOTP();
        session.setAttribute("newAccount",Account.builder()
                .email(email)
                .password(password)
                .build());
        session.setAttribute("register", newUser);
        session.setAttribute("OTP", otp);

        emailService.SendEmail(email, "OTP alohcmute, please don't leak this!", otp);
        model.addAttribute("newUser", newUser);
        return "web/authen-OTP";
    }

    @PostMapping("/register/OTP")
    public String registerOTP(@Param("otp") String otp, HttpSession session) {
        // authen otp not correct return to register
        if(!session.getAttribute("OTP").equals(otp)){
            return "redirect:/auth/register";
        }
        // save user
        User user = (User) session.getAttribute("register");
        Account account = (Account) session.getAttribute("newAccount");
        user.setCreateDate(new Date(System.currentTimeMillis()));
        account.setUser(user);
        account.setRole(Role.USER);
        // save new acc and user
        authenticationService.register(account, user);
        System.out.println("done");
        return "redirect:/auth/login";
    }

}