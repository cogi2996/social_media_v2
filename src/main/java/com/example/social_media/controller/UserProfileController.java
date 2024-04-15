package com.example.social_media.controller;

import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//note here
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    @GetMapping
    public String getIntoProfile(@RequestParam("id") Integer id,Model model) {
        User currentUser = authenticationFacade.getUser();
        // cau truc re nhanh + redirect khi la khách, redirect khi là chính chủ
        User user = userService.findUserById(id);
        // error case not found id
        if (user == null) {
            return "web/pages-error";
        }
        // case visiter is not owner profile

        // case owner is owner profile
        model.addAttribute("user",user);
        return "web/profile";
    }

    @GetMapping("/edit")
    public String getIntoEditProfile(Model model) {
        User currentUser = authenticationFacade.getUser();
        model.addAttribute("curUser",currentUser);
        return "web/form-wizard-vertical";
    }



}
