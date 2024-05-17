package com.example.social_media.controller;

import com.example.social_media.entity.EntityId.FollowId;
import com.example.social_media.entity.Follow;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.FollowService;
import com.example.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final FollowService followService;
    @GetMapping
    public String getIntoFollowPage(Model model) {

        User currentUser = authenticationFacade.getUser();
        model.addAttribute("user", currentUser);
        List<User> pendingFollow = userService.findPendingFollowingById(currentUser.getUserId(), 0, 7, null);

        if (!pendingFollow.isEmpty()) {
            model.addAttribute("followerPending",pendingFollow);
        } else {
            model.addAttribute("message", "Bạn không có yêu cầu theo dõi nào!");
        }
        return "web/friend-request";
    }

    @GetMapping("/deleteFollow/{userId}")
    public String deleteFollowConnection(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        int currentUserId = authenticationFacade.getUser().getUserId();
        User user = userService.findUserById(userId);
        if (user == null) {
            return "web/pages-error";
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
            return "web/pages-error";
        }
        followService.save(new Follow( new FollowId(currentUserId, userId )));
        redirectAttributes.addAttribute("currentUserId", currentUserId);
        return "redirect:/{currentUserId}/connection";
    }

    // huỷ follow ở trang follow
    @PostMapping("/deleteFollowPending/{userId}")
    public String deleteFollowPending(@PathVariable Integer userId) {
        int currentUserId = authenticationFacade.getUser().getUserId();
        User user = userService.findUserById(userId);
        if (user == null) {
            return "web/pages-error";
        }
        followService.deleteFollow(FollowId.builder()
                .sourceId(userId)
                .targetId(currentUserId)
                .build());
        return "redirect:/follows";
    }

    @PostMapping("/acceptFollowPending/{userId}")
    public String acceptFollowPending(@PathVariable Integer userId) {
        int currentUserId = authenticationFacade.getUser().getUserId();
        User user = userService.findUserById(userId);
        if (user == null) {
            return "web/pages-error";
        }
        followService.acceptFollow(FollowId.builder()
                .sourceId(userId)
                .targetId(currentUserId)
                .build());
        return "redirect:/follows";
    }



}
