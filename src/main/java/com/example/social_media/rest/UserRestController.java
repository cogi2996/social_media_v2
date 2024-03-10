package com.example.social_media.rest;


import com.example.social_media.DTO.GroupDTO;
import com.example.social_media.DTO.UserDTO;
import com.example.social_media.entity.EntityId.LikePostId;
import com.example.social_media.entity.Follow;
import com.example.social_media.entity.EntityId.FollowId;
import com.example.social_media.entity.LikePost;
import com.example.social_media.entity.Post;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.FollowService;
import com.example.social_media.service.LikePostService;
import com.example.social_media.service.PostService;
import com.example.social_media.service.IUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserService userService;
    private final PostService postService;
    private final  FollowService followService;
    private final LikePostService likePostService;
    private final IAuthenticationFacade authenticationFacade;
    private ModelMapper modelMapper;
    @GetMapping
    public ResponseEntity<List<User>> getAllEmployees(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "2") Integer pageSize,
            @RequestParam(defaultValue = "userId") String sortBy)
    {

        Page<User> users = userService.findAll(pageNum, pageSize, sortBy);
        if(pageNum >= users.getTotalPages()){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(users.getContent());
    }


//    @GetMapping
//    public ResponseEntity<List<User>> getAllEmployees(
//            @RequestParam(defaultValue = "0") Integer pageNo,
//            @RequestParam(defaultValue = "2") Integer pageSize,
//            @RequestParam(defaultValue = "userId") String sortBy)
//    {
//        List<User> list = userService.findAll(pageNo, pageSize, sortBy);
//
//        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
//    }


    @GetMapping("/{userId}/groups")
    public ResponseEntity<GroupDTO> getUserGroups(@PathVariable int userId) {
        User user = userService.findUserById(userId);

        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return null;
    }

    @PostMapping("/{userId}/followingUsers/{targetId}")
    public ResponseEntity<Follow> followUserById(@PathVariable int userId, @PathVariable("targetId") int targetId) {
        User sourceUser = userService.findUserById(userId);
        User targetUser = userService.findUserById(targetId);
        System.out.println(targetId);
        // get current user authenticated
        int dbUserId = authenticationFacade.getUser().getUserId();
        System.out.println("here end point");
        if (sourceUser == null || targetUser == null|| dbUserId != userId){
            return ResponseEntity.badRequest().body(null);
        }
        Follow follow = new Follow(new FollowId(userId, targetId));
        followService.save(follow);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/{userId}/followingUsers/{targetId}")
    public ResponseEntity<Void> unfollow(@PathVariable int userId, @PathVariable("targetId") int targetId) {
        User sourceUser = userService.findUserById(userId);
        User targetUser = userService.findUserById(targetId);
        int dbUserId = authenticationFacade.getUser().getUserId();
        System.out.println("here end point");
        if (sourceUser == null || targetUser == null|| dbUserId != sourceUser.getUserId()){
            return ResponseEntity.badRequest().body(null);
        }
        FollowId follow = new FollowId(userId, targetId);
        followService.deleteFollow(follow);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{userId}/followers/{targetId}")
    public ResponseEntity<Void> acceptFollow(@PathVariable int userId, @PathVariable("targetId") int targetId) {

        try {
            // kiểm tra id
            User sourceUser = userService.findUserById(userId);
            User targetUser = userService.findUserById(targetId);
            int dbUserId = authenticationFacade.getUser().getUserId();
            System.out.println("here end point");
            if (sourceUser == null || targetUser == null|| dbUserId != sourceUser.getUserId()){
                return ResponseEntity.badRequest().body(null);
            }
            FollowId follow = new FollowId(targetId,userId);
            followService.acceptFollow(follow);
            return ResponseEntity.ok().build();
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().build();

    }
    @PostMapping("/{userId}/likeList/posts/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable int userId,@PathVariable int postId){
        // Kiểm tra tồn tại của user và post
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);
        if(user == null || post == null){
            return ResponseEntity.noContent().build();
        }
        // nếu tìm thấy user và post
        LikePostId likePostId = new LikePostId(userId,postId);
        LikePost likePost = new LikePost(likePostId, LocalDateTime.now());
        likePostService.save(likePost);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{userId}/likeList/posts/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable int userId,@PathVariable int postId){
        // Kiểm tra tồn tại của user và post
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);
        if(user == null || post == null){
            return ResponseEntity.noContent().build();
        }
        // nếu tìm thấy user và post
        LikePostId likePostId = new LikePostId(userId,postId);
        likePostService.deleteById(likePostId);
        return ResponseEntity.ok().build();
    }

    private User convertToEntity(UserDTO userDTO){
        return modelMapper.map(userDTO,User.class);
    }




}
