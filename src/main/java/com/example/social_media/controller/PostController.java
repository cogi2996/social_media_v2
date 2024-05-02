package com.example.social_media.controller;

import com.example.social_media.entity.Post;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.LikePostService;
import com.example.social_media.service.PostService;
import com.example.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final PostService postService;
    private final LikePostService likePostService;
//    @GetMapping("/{userId}/post/{postId}")
    @GetMapping("/post/{postId}")
    // check tra đã follow thì mới được xem
    public String getIntoIndex(Model model, @PathVariable("postId") Integer postId ){

        Post post = postService.findOne(postId);
        // post không tồn tại || không chính chủ

        model.addAttribute("user",authenticationFacade.getUser());
        model.addAttribute("post",post);
        model.addAttribute("countLike",likePostService.countLikesByPostId(postId));
        model.addAttribute("isLiked",likePostService.existsLikedPostByPostIdAndUserId(postId,authenticationFacade.getUser().getUserId()));
        return "web/single-post";
    }

}
