package com.example.social_media.rest;

import com.example.social_media.DTO.PostDTO;
import com.example.social_media.DTO.ResponseDTO;
import com.example.social_media.DTO.UserDTO;
import com.example.social_media.Utils.ConvertToDTO;
import com.example.social_media.Utils.ConvertToEntity;
import com.example.social_media.entity.LikePost;
import com.example.social_media.entity.Post;
import com.example.social_media.entity.User;
import com.example.social_media.security.AuthenticationFacade;
import com.example.social_media.service.LikePostService;
import com.example.social_media.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final AuthenticationFacade authenticationFacade;
    private final PostService postService;
    private final ConvertToEntity convertToEntity;
    private final ConvertToDTO convertToDTO;
    private final LikePostService likePostService;

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
    public ResponseEntity<?> getNewPost(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "postCreateTime") String sortBy
    ) {
        User user = authenticationFacade.getUser();
        int userId = user.getUserId();
        List<Integer> followingId = user.getFollowingUsers().stream().map(User::getUserId).toList();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        List<Post> posts =  postService.findPostsByUserIdAndFollowerIds(userId, followingId, pageable);
        ObjectMapper mapper  = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule( ));
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        List<ObjectNode> postDTOS = posts.stream().map(post -> {
            int postId = post.getPostId();
            Boolean liked =likePostService.existsLikedPostByPostIdAndUserId(postId, userId);
            PostDTO postDTO = convertToDTO.convertToDTO(post);
            System.out.println("postTime:"+ post.getPostCreateTime());
            System.out.println("postDTOTime:"+ postDTO.getPostCreateTime());
            postDTO.setPostCreateTime(post.getPostCreateTime().atZone(ZoneId.of("GMT+7")));
            postDTO.setCountLike(likePostService.countLikesByPostId(postId));
            UserDTO userDTO = convertToDTO.convertToDTO(post.getUser());
            postDTO.setUserDTO(userDTO);
            ObjectNode node = mapper.valueToTree(postDTO);
            node.put("liked",liked);
            System.out.println("count like: "+likePostService.countLikesByPostId(postId));
            return node;
        }).toList();
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .data(postDTOS)
                .build());

    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Integer> getCountLikePost(@PathVariable int postId) {
        return ResponseEntity.ok(likePostService.countLikesByPostId(postId));
    }


}
