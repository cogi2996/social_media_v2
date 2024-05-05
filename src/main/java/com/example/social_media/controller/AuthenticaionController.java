package com.example.social_media.controller;

import com.example.social_media.DTO.AuthenticationRequest;
import com.example.social_media.Utils.OTPGenerator;
import com.example.social_media.dao.AccountRepository;
import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.Account;
import com.example.social_media.entity.User;
import com.example.social_media.security.AuthenticationFacade;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.security.Role;
import com.example.social_media.service.AuthenticationService;
import com.example.social_media.service.EmailService;
import jakarta.persistence.Cacheable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        model.addAttribute("request", request);
        return "web/sign-in";
    }

    @PostMapping("/getAuth")
    public String getAuth(@ModelAttribute AuthenticationRequest request, HttpSession session, @NonNull HttpServletRequest req, HttpServletResponse resp) {
        authenticationService.baseAuthenticate(request, req);
        User user = accountRepository.findByEmail(request.getEmail()).get().getUser();
        session.setAttribute("user", user);
        return "redirect:/home/index";
    }


    @GetMapping("/register")
    public String getIntoRegister(Model model,@Param("error") String error) {
        User user = new User();
        model.addAttribute("newUser", user);
        System.out.println("before");
        if(error != null){
            System.out.println("after");
            model.addAttribute("message",error);
        }
        return "web/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User newUser, HttpSession session, @Param("password") String password, @Param("email") String email, Model model, RedirectAttributes redirectAttributes) {
        if(accountRepository.findByEmail(email).isPresent()){
            System.out.println("Email đã tồn tại");
            redirectAttributes.addAttribute("message","Email đã tồn tại");
            return "redirect:/auth/register?error={message}";
        }
        System.out.println("bypass");
        //validate pass
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
            session.invalidate();
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