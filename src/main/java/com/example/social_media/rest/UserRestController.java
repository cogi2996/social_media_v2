//package com.example.social_media.rest;
//
//
//import com.example.social_media.entity.EntityId.LikePostId;
//import com.example.social_media.entity.Follow;
//import com.example.social_media.entity.EntityId.FollowId;
//import com.example.social_media.entity.LikePost;
//import com.example.social_media.entity.Post;
//import com.example.social_media.entity.User;
//import com.example.social_media.service.FollowService;
//import com.example.social_media.service.LikePostService;
//import com.example.social_media.service.PostService;
//import com.example.social_media.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.repository.query.Param;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserRestController {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private FollowService followService;
//
//    @Autowired
//    private LikePostService likePostService;
//
//    @PostMapping("/{userId}/followingUsers")
//    public ResponseEntity<Follow> follow(@PathVariable String userId, @Param("targetId") String targetId) {
//        User sourceUser = userService.findUserById(userId);
//        User targetUser = userService.findUserById(targetId);
//        System.out.println(targetId);
//
//        if (sourceUser == null || targetUser == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//        Follow follow = new Follow(new FollowId(userId, targetId));
//        followService.save(follow);
//        return ResponseEntity.ok(follow);
//    }
//
//    @DeleteMapping("/{userId}/followingUsers")
//    public ResponseEntity<Void> unfollow(@PathVariable String userId, @Param("targetId") String targetId) {
//        User sourceUser = userService.findUserById(userId);
//        User targetUser = userService.findUserById(targetId);
//        if (sourceUser == null || targetUser == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//        FollowId follow = new FollowId(userId, targetId);
//        followService.deleteFollow(follow);
//        return ResponseEntity.ok().build();
//    }
//    @PatchMapping("/{userId}/followers")
//    public ResponseEntity<Void> acceptFollow(@PathVariable String userId, @Param("targetId") String targetId) {
//
//        try {
//            FollowId follow = new FollowId(targetId,userId);
//            followService.acceptFollow(follow);
//            return ResponseEntity.ok().build();
//        }catch (RuntimeException ex){
//            ex.printStackTrace();
//        }
//        return ResponseEntity.badRequest().build();
//
//    }
//    @PostMapping("/{userId}/likeList/posts/{postId}")
//    public ResponseEntity<Void> likePost(@PathVariable String userId,@PathVariable int postId){
//        // Kiểm tra tồn tại của user và post
//        User user = userService.findUserById(userId);
//        Post post = postService.findPostById(postId);
//        if(user == null || post == null){
//            return ResponseEntity.noContent().build();
//        }
//        // nếu tìm thấy user và post
//        LikePostId likePostId = new LikePostId(userId,postId);
//        LikePost likePost = new LikePost(likePostId, LocalDateTime.now());
//        likePostService.save(likePost);
//        return ResponseEntity.ok().build();
//    }
//    @DeleteMapping("/{userId}/likeList/posts/{postId}")
//    public ResponseEntity<Void> unlikePost(@PathVariable String userId,@PathVariable int postId){
//        // Kiểm tra tồn tại của user và post
//        User user = userService.findUserById(userId);
//        Post post = postService.findPostById(postId);
//        if(user == null || post == null){
//            return ResponseEntity.noContent().build();
//        }
//        // nếu tìm thấy user và post
//        LikePostId likePostId = new LikePostId(userId,postId);
//        likePostService.deleteById(likePostId);
//        return ResponseEntity.ok().build();
//    }
//
//
//
//
//
//}
