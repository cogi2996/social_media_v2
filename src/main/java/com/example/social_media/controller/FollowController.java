package com.example.social_media.controller;

import com.example.social_media.entity.EntityId.FollowId;
import com.example.social_media.entity.Follow;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.FollowService;
import com.example.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/deleteFollow/{userId}")
    public String deleteFollow(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        int currentUserId = authenticationFacade.getUser().getUserId();
        User user = userService.findUserById(userId);
        if (user == null) {
            return "dashboard/pages-error";
        }
        followService.deleteFollow(new FollowId(currentUserId, userId ));
        redirectAttributes.addAttribute("currentUserId", currentUserId);
        return "redirect:/{currentUserId}/connection";
    }

    @GetMapping("/requestFollow/{userId}")
    public String acceptFollow(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        int currentUserId = authenticationFacade.getUser().getUserId();
        User user = userService.findUserById(userId);
        if (user == null) {
            return "dashboard/pages-error";
        }
        followService.save(new Follow( new FollowId(currentUserId, userId )));
        redirectAttributes.addAttribute("currentUserId", currentUserId);
        return "redirect:/{currentUserId}/connection";
    }


}
