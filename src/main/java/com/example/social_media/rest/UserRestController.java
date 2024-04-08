package com.example.social_media.rest;


import com.example.social_media.DTO.GroupDTO;
import com.example.social_media.DTO.PostDTO;
import com.example.social_media.DTO.ResponseDTO;
import com.example.social_media.DTO.UserDTO;
import com.example.social_media.Utils.ConvertToDTO;
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
import com.example.social_media.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.DefaultParameters;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {
    private final ObjectMapper mapper;
    private final UserService userService;
    private final PostService postService;
    private final FollowService followService;
    private final LikePostService likePostService;
    private final IAuthenticationFacade authenticationFacade;
    private final ModelMapper modelMapper;
    private final ConvertToDTO convertToDTO;

    @GetMapping
    public ResponseEntity<List<User>> getAllEmployees(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "2") Integer pageSize,
            @RequestParam(defaultValue = "userId") String sortBy) {

        Page<User> users = userService.findAll(pageNum, pageSize, sortBy);
        if (pageNum >= users.getTotalPages()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(users.getContent());
    }


    @GetMapping("/{userId}/groups")
    public ResponseEntity<GroupDTO> getUserGroups(@PathVariable int userId) {
        User user = userService.findUserById(userId);

        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return null;
    }

    @PostMapping("/{sourceId}/follows/{targetId}")
    public ResponseEntity<?> followUserById(@PathVariable int sourceId, @PathVariable("targetId") int targetId) {
        User sourceUser = userService.findUserById(sourceId);
        User targetUser = userService.findUserById(targetId);
        System.out.println(sourceId + " " + targetId);
        // get current user authenticated
        int curUserId = authenticationFacade.getUser().getUserId();
        if (sourceUser == null || targetUser == null || curUserId != sourceId) {
            return ResponseEntity.badRequest().body(null);
        }
        Follow follow = new Follow(new FollowId(sourceId, targetId));
        followService.save(follow);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .build()
        );
    }

    @DeleteMapping("/{userId}/follows/{targetId}")
    public ResponseEntity<?> unfollow(@PathVariable int userId, @PathVariable("targetId") int targetId) {
        User sourceUser = userService.findUserById(userId);
        User targetUser = userService.findUserById(targetId);
        int dbUserId = authenticationFacade.getUser().getUserId();
        if (sourceUser == null || targetUser == null || dbUserId != sourceUser.getUserId()) {
            return ResponseEntity.badRequest().body(null);
        }
        FollowId follow = new FollowId(userId, targetId);
        followService.deleteFollow(follow);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .build()
        );
    }

    @PatchMapping("/{userId}/followers/{targetId}")
    public ResponseEntity<Void> acceptFollow(@PathVariable int userId, @PathVariable("targetId") int targetId) {

        try {
            // kiểm tra id
            User sourceUser = userService.findUserById(userId);
            User targetUser = userService.findUserById(targetId);
            int dbUserId = authenticationFacade.getUser().getUserId();
            System.out.println("here end point");
            if (sourceUser == null || targetUser == null || dbUserId != sourceUser.getUserId()) {
                return ResponseEntity.badRequest().body(null);
            }
            FollowId follow = new FollowId(targetId, userId);
            followService.acceptFollow(follow);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/{userId}/likeList/posts/{postId}")
    public ResponseEntity<?> likePost(@PathVariable int userId, @PathVariable int postId) {
        // Kiểm tra tồn tại của user và post
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);
        if (user == null || post == null) {
            return ResponseEntity.noContent().build();
        }
        // nếu tìm thấy user và post
        LikePostId likePostId = new LikePostId(userId, postId);
        LikePost likePost = LikePost.builder().likePostId(likePostId).build();
        likePostService.save(likePost);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .data(likePostService.countLikesByPostId(postId))
                .build());
    }

    @DeleteMapping("/{userId}/likeList/posts/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable int userId, @PathVariable int postId) {
        // Kiểm tra tồn tại của user và post
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);
        if (user == null || post == null) {
            return ResponseEntity.noContent().build();
        }
        // nếu tìm thấy user và post
        LikePostId likePostId = new LikePostId(userId, postId);
        likePostService.deleteById(likePostId);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .data(likePostService.countLikesByPostId(postId))
                .build());
    }

    // get list follower this user but don't know current user follow or not
    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable int userId,
                                          @RequestParam(defaultValue = "0") Integer pageNum,
                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                          @RequestParam(defaultValue = "userId") String sortBy,
                                          @RequestParam(defaultValue = "") String curentUserId
    ) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        List<User> followers = userService.findFollowersByUserId(userId, pageNum, pageSize, sortBy);
         if (!curentUserId.isEmpty()) {
            List<ObjectNode> followersDTO = followers
                    .stream()
                    .map(u -> {
                        int followerId = u.getUserId();
                        Boolean followed = followService.existsFollowBySourceIdAndTargetId(Integer.parseInt(curentUserId),followerId);
                        UserDTO userDTO = convertToDTO.convertToDTO(u);
                        ObjectNode node = mapper.valueToTree(userDTO);
                        node.put("isFollowed", followed);
                        return node;
                    }).toList();
            return ResponseEntity.ok().body(ResponseDTO.builder()
                    .message("success")
                    .data(followersDTO)
                    .build());
        }
        return ResponseEntity.ok().body(ResponseDTO.builder()
                .message("success")
                .data(followers.stream().map(u -> modelMapper.map(u, UserDTO.class)).toList())
                .build());
    }

    // get list following but don't know current user follow or not
    @GetMapping("/{userId}/followings")
    public ResponseEntity<?> getFollowing(@PathVariable int userId,
                                          @RequestParam(defaultValue = "0") Integer pageNum,
                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                          @RequestParam(defaultValue = "userId") String sortBy,
                                          @RequestParam(defaultValue = "") String curentUserId
    ) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        List<User> followings = userService.findFollowingUserByUserId(userId, pageNum, pageSize, sortBy);
        if (!curentUserId.isEmpty()) {
            List<ObjectNode> followersDTO = followings
                    .stream()
                    .map(u -> {
                        int followerId = u.getUserId();
                        Boolean followed = followService.existsFollowBySourceIdAndTargetId(Integer.parseInt(curentUserId),followerId);
                        UserDTO userDTO = convertToDTO.convertToDTO(u);
                        ObjectNode node = mapper.valueToTree(userDTO);
                        node.put("isFollowed", followed);
                        return node;
                    }).toList();
            return ResponseEntity.ok().body(ResponseDTO.builder()
                    .message("success")
                    .data(followersDTO)
                    .build());
        }
        return ResponseEntity.ok().body(ResponseDTO.builder()
                .message("success")
                .data(followings.stream().map(u -> modelMapper.map(u, UserDTO.class)).toList())
                .build());
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<ObjectNode>> getPostProfile(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "postCreateTime") String sortBy
    ) {
        User user = authenticationFacade.getUser();
        int userId = user.getUserId();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        List<Post> posts = postService.findPostsByUserId(userId, pageable);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ObjectNode> postDTOS = posts.stream().map(post -> {
            int postId = post.getPostId();
            Boolean liked = likePostService.existsLikedPostByPostIdAndUserId(postId, userId);
            PostDTO postDTO = convertToDTO.convertToDTO(post);
            postDTO.setCountLike(likePostService.countLikesByPostId(postId));
            UserDTO userDTO = convertToDTO.convertToDTO(post.getUser());
            postDTO.setUserDTO(userDTO);
            ObjectNode node = mapper.valueToTree(postDTO);
            node.put("liked", liked);
            return node;
        }).toList();
        return ResponseEntity.ok(postDTOS);

    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            System.out.println(userDTO);
            User user = modelMapper.map(userDTO, User.class);
            user.setUserId(authenticationFacade.getUser().getUserId());
            User updatedUser = userService.update(user);
            responseDTO.setMessage("User updated successfully.");
            responseDTO.setData(updatedUser);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user.");
        }
    }

}
