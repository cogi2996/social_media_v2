package com.example.social_media.controller;

import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.FollowService;
import com.example.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ConnectionController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final FollowService followService;
    @GetMapping("{userId}/connection")
    public String getIntoIndex(@PathVariable Integer userId, Model model) {
        model.addAttribute("userId",userService.findUserById(userId).getUserId());
        model.addAttribute("countFollowers",followService.countByFollowId_TargetIdAndFollowStatus(userId,true));
        model.addAttribute("countFollowings",followService.countByFollowId_SourceIdAndFollowStatus(userId,true));
        System.out.println("countFollowers: "+followService.countByFollowId_TargetIdAndFollowStatus(userId,true));
        System.out.println("countFollowings: "+followService.countByFollowId_SourceIdAndFollowStatus(userId,true));
        return "app/connection";
    }

    // huỷ theo dõi




}
