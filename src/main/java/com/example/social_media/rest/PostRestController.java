package com.example.social_media.rest;

import com.example.social_media.DTO.PostDTO;
import com.example.social_media.Utils.ConvertToDTO;
import com.example.social_media.Utils.ConvertToEntity;
import com.example.social_media.entity.Post;
import com.example.social_media.entity.User;
import com.example.social_media.security.AuthenticationFacade;
import com.example.social_media.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final AuthenticationFacade authenticationFacade;
    private final PostService postService;
    private final ConvertToEntity convertToEntity;
    private final ConvertToDTO convertToDTO;
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO post) {
        // mapping to entity
        Post newPost = convertToEntity.convertToEntity(post);
        // get current authenticated user
        User user = authenticationFacade.getUser();
        // set post user to current authenticated user
        newPost.setUser(user);
        newPost.setPostCreateTime(new Date(System.currentTimeMillis()));
        // save post
        postService.createPost(newPost);
        // response for client
        return ResponseEntity.ok().body(convertToDTO.convertToDTO(newPost));
    }
}
