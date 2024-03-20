package com.example.social_media.controller;

import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final IAuthenticationFacade authenticationFacade;
    @RequestMapping("/index")
    public String getIntoIndex(Model model,HttpSession session) {
        User sessionUser = (User)session.getAttribute("user");
        System.out.println("index session user "+sessionUser.getFirstName());
        System.out.println("full name user in index board");
        System.out.println(authenticationFacade.getUser().getFullName());
        return "dashboard/index";
    }
}
