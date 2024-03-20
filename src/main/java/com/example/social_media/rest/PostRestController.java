package com.example.social_media.rest;

import com.example.social_media.DTO.PostDTO;
import com.example.social_media.DTO.UserDTO;
import com.example.social_media.Utils.ConvertToDTO;
import com.example.social_media.Utils.ConvertToEntity;
import com.example.social_media.entity.Post;
import com.example.social_media.entity.User;
import com.example.social_media.security.AuthenticationFacade;
import com.example.social_media.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    // cho phép các domain khác gọi API này
    @CrossOrigin(origins = "*")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO post) {
        Post newPost = convertToEntity.convertToEntity(post);
        // get current authenticated user
        User user = authenticationFacade.getUser();
        // set post user to current authenticated user
        newPost.setUser(user);
        newPost.setPostCreateTime(new Date(System.currentTimeMillis()));
        postService.createPost(newPost);
        PostDTO postDTO = convertToDTO.convertToDTO(newPost);
        UserDTO userDTO = convertToDTO.convertToDTO(user);
        postDTO.setUserDTO(userDTO);
        return ResponseEntity.ok().body(postDTO);
    }
}
