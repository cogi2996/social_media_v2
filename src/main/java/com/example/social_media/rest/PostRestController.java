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
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final AuthenticationFacade authenticationFacade;
    private final PostService postService;
    private final ConvertToEntity convertToEntity;
    private final ConvertToDTO convertToDTO;

    // cho phép các domain khác gọi API này
    @PostMapping
//    @CrossOrigin(origins = "*")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO post) {
        Post newPost = convertToEntity.convertToEntity(post);
        // get current authenticated user
        User user = authenticationFacade.getUser();
        // set post user to current authenticated user
        newPost.setUser(user);
//        newPost.setPostCreateTime(System.currentTimeMillis());
        postService.createPost(newPost);
        PostDTO postDTO = convertToDTO.convertToDTO(newPost);
        UserDTO userDTO = convertToDTO.convertToDTO(user);
        postDTO.setUserDTO(userDTO);
        return ResponseEntity.ok().body(postDTO);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getNewPost(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "postCreateTime") String sortBy
    ) {
        User user = authenticationFacade.getUser();
        int userId = user.getUserId();
        List<Integer> followerIds = user.getFollowingUsers().stream().map(User::getUserId).toList();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        List<Post> posts =  postService.findPostsByUserIdAndFollowerIds(userId, followerIds, pageable);
        List<PostDTO> postDTOS = posts.stream().map(post -> {
            PostDTO postDTO = convertToDTO.convertToDTO(post);
            UserDTO userDTO = convertToDTO.convertToDTO(post.getUser());
            postDTO.setUserDTO(userDTO);
            return postDTO;
        }).toList();
        return ResponseEntity.ok(postDTOS);

    }



}
