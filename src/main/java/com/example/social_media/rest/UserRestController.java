package com.example.social_media.rest;


import com.example.social_media.dao.FollowRepository;
import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.Follow;
import com.example.social_media.entity.FollowId;
import com.example.social_media.entity.User;
import com.example.social_media.service.FollowService;
import com.example.social_media.service.UserService;
import com.example.social_media.service.UserServiceImpl;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @PostMapping("/{userId}/followingUsers")
    public ResponseEntity<Follow> follow(@PathVariable String userId, @Param("targetId") String targetId) {
        User sourceUser = userService.findUserById(userId);
        User targetUser = userService.findUserById(targetId);
        System.out.println(targetId);

        if (sourceUser == null || targetUser == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Follow follow = new Follow(new FollowId(userId, targetId));
        followService.save(follow);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/{userId}/followingUsers")
    public ResponseEntity<Void> unfollow(@PathVariable String userId, @Param("targetId") String targetId) {
        User sourceUser = userService.findUserById(userId);
        User targetUser = userService.findUserById(targetId);
        if (sourceUser == null || targetUser == null) {
            return ResponseEntity.badRequest().body(null);
        }
        FollowId follow = new FollowId(userId, targetId);
        followService.deleteFollow(follow);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{userId}/followers")
    public ResponseEntity<Void> acceptFollow(@PathVariable String userId, @Param("targetId") String targetId) {

        try {
            FollowId follow = new FollowId(targetId,userId);
            followService.acceptFollow(follow);
            return ResponseEntity.ok().build();
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().build();

    }




}
