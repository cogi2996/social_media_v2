package com.example.social_media.controller;

import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    @GetMapping("/index")
    public String getIntoIndex(Model model, HttpSession session,
                               @RequestParam(defaultValue = "0") Integer pageNum,@RequestParam(defaultValue = "4") Integer pageSize) {
        User currentUser = authenticationFacade.getUser();
        model.addAttribute("user",currentUser);
        model.addAttribute("recommendUsers",userService.findPeopleNotFollowedByUserId(currentUser.getUserId(),pageNum,pageSize));
//        System.out.println("ROLE"+authenticationFacade.getRole());
//        return "dashboard/index";
        return "web/index";
    }




}
