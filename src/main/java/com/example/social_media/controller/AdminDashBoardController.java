package com.example.social_media.controller;

import com.example.social_media.entity.Account;
import com.example.social_media.entity.Post;
import com.example.social_media.entity.User;
import com.example.social_media.service.AccountService;
import com.example.social_media.service.PostService;
import com.example.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashBoardController {
    private final UserService userService;
    private final AccountService accountService;
    private final PostService postService;


    @GetMapping("/userControll")
    public String adminPage(
            Model model,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "6") Integer pageSize
    ) {
        List<User> users = userService.findAll(pageNum, pageSize, null).getContent();
        List<Boolean> isLocked = users.stream().map(user -> !user.getAccount().getStatus()).toList();
        model.addAttribute("users", users);
        int totalPages = userService.findAll(pageNum, pageSize, null).getTotalPages();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("isLocked", isLocked);
        return "admin/userControll";
    }

    @GetMapping("/lockUser")
    public String lockUser(
            Model model,
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "6") Integer pageSize,
            RedirectAttributes redirectAttributes
    ) {
        User user = userService.findUserById(userId);
        Account account = user.getAccount();
        account.setStatus(!account.getStatus());
        accountService.save(account);
        // tongle status
        List<User> users = userService.findAll(pageNum, pageSize, null).getContent();
        model.addAttribute("users", users);
        int totalPages = userService.findAll(pageNum, pageSize, null).getTotalPages();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNum);
        redirectAttributes.addAttribute("pageNum", pageNum);
        return "redirect:/admin/userControll?pageNum={pageNum}";
    }

    @GetMapping("/userDetail")
    public String userDetail(
            Model model,
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") Integer pageNum
    ) {
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("account", user.getAccount());
        model.addAttribute("currentPage", pageNum);
        return "admin/userDetail";
    }

    @GetMapping("/postControll")
    public String postControllPage(
            Model model,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "6") Integer pageSize
    ) {
        List<Post> posts = postService.findAllPosts(pageNum, pageSize, null).getContent();
        int totalPages = postService.findAllPosts(pageNum, pageSize, null).getTotalPages();
        List<Boolean> isLocked = posts.stream().map(p -> !p.isStatus()).toList();
        model.addAttribute("posts", posts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("isLocked", isLocked);
        System.out.println("Total pages: " + postService.countAllPosts());
        return "admin/postControll";
    }

    @GetMapping("/lockPost")
    public String lockPost(
            Model model,
            @RequestParam Integer postId,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "6") Integer pageSize,
            RedirectAttributes redirectAttributes
    ) {
        Post post = postService.findOne(postId);
        post.setStatus(!post.isStatus());
        postService.save(post);
        List<Post> posts = postService.findAllPosts(pageNum, pageSize, null).getContent();
        int totalPages = postService.findAllPosts(pageNum, pageSize, null).getTotalPages();
        List<Boolean> isLocked = posts.stream().map(p -> !p.isStatus()).toList();
        redirectAttributes.addAttribute("pageNum", pageNum);
        return "redirect:/admin/postControll?pageNum={pageNum}";
    }

    @GetMapping("/postDetail")
    public String postDetail(
            Model model,
            @RequestParam Integer postId,
            @RequestParam(defaultValue = "0") Integer pageNum
    ) {
        Post post = postService.findOne(postId);
        model.addAttribute("post", post);
        model.addAttribute("currentPage", pageNum);
        return "admin/postDetail";
    }

    @GetMapping("/deletePost")
    public String deletePost(
            Model model,
            @RequestParam Integer postId,
            @RequestParam(defaultValue = "0") Integer pageNum,
            RedirectAttributes redirectAttributes
    ) {
        postService.deleteByPostId(postId);
        redirectAttributes.addAttribute("currentPage", pageNum);
        return "redirect:/admin/postControll?pageNum={currentPage}";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(
            Model model,
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") Integer pageNum,
            RedirectAttributes redirectAttributes
    ) {
        userService.delete(userId);
        redirectAttributes.addAttribute("pageNum", pageNum);
        return "redirect:/admin/userControll?pageNum={pageNum}";
    }
}
