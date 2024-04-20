package com.example.social_media.rest;


import com.example.social_media.DTO.*;
import com.example.social_media.Utils.ConvertToDTO;
import com.example.social_media.entity.*;
import com.example.social_media.entity.EntityId.LikePostId;
import com.example.social_media.entity.EntityId.FollowId;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

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
    private final NotificationLikeService notificationLikeService;
    private final NotificationService notificationService;


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable int userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(ResponseDTO.builder()
                .message("success")
                .data(user)
                .build());
    }

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
        if (sourceUser == null || targetUser == null || dbUserId != targetId) {
            return ResponseEntity.badRequest().body(null);
        }
        FollowId follow = FollowId.builder()
                .sourceId(userId)
                .targetId(targetId)
                .build();
        followService.deleteFollow(follow);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .build()
        );
    }

    @PatchMapping("/{userId}/followers/{targetId}")
    public ResponseEntity<?> acceptFollow(@PathVariable int userId, @PathVariable("targetId") int targetId) {

        try {
            // kiểm tra id
            User sourceUser = userService.findUserById(userId);
            User targetUser = userService.findUserById(targetId);
            int dbUserId = authenticationFacade.getUser().getUserId();
            System.out.println("here end point");
            if (sourceUser == null || targetUser == null || dbUserId != targetId) {
                return ResponseEntity.badRequest().body(null);
            }
            FollowId follow = FollowId.builder()
                    .sourceId(userId)
                    .targetId(targetId)
                    .build();
            followService.acceptFollow(follow);
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .message("success")
                    .build();
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/{userId}/likeList/posts/{postId}")
    //viết thêm logic aop chỉ A follow B hoặc A là admin mới được call api này
    public ResponseEntity<?> likePost(@PathVariable int userId, @PathVariable int postId) {
        // Kiểm tra tồn tại của user và post
        User user = userService.findUserById(userId);
        Post post = postService.findOne(postId);
        if (user == null || post == null) {
            return ResponseEntity.noContent().build();
        }
        // nếu tìm thấy user và post
        LikePostId likePostId = new LikePostId(userId, postId);
        LikePost likePost = LikePost.builder().likePostId(likePostId).build();
        likePostService.save(likePost);

        //like bài viết từ một người khác
        if (userId != post.getUser().getUserId()) {
            // thông báo đến người được like
            Notification notification = Notification.builder()
                    .type(TypeAnnounce.LIKE)
                    .user(post.getUser())
                    .status(false)
                    .build();
            NotificationLikePost notificationLikePost = NotificationLikePost.builder()
                    .post(post)
                    .notification(notification)
                    .userLiked(user)
                    .build();
            notificationLikeService.save(notificationLikePost);
        }


        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .data(likePostService.countLikesByPostId(postId))
                .build());
    }

    @DeleteMapping("/{userId}/likeList/posts/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable int userId, @PathVariable int postId) {
        // Kiểm tra tồn tại của user và post
        User user = userService.findUserById(userId);
        Post post = postService.findOne(postId);
        if (user == null || post == null) {
            return ResponseEntity.noContent().build();
        }
        // nếu tìm thấy user và post
        LikePostId likePostId = new LikePostId(userId, postId);
        likePostService.deleteById(likePostId);
        // huỷ thông báo follow trừ thằng chủ bài viết thì không xoá vì không có
        NotificationLikePost notify =   notificationLikeService.findByUserLikedId(userId, postId);
        if (notify != null&& userId != post.getUser().getUserId()){
            notificationLikeService.delete(notify);
        }
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
                        Integer followed = followService.existsFollowBySourceIdAndTargetId(Integer.parseInt(curentUserId), followerId);
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
                        Integer followed = followService.existsFollowBySourceIdAndTargetId(Integer.parseInt(curentUserId), followerId);
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
    public ResponseEntity<?> getPostProfile(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "postCreateTime") String sortBy
    ) {
        User user = authenticationFacade.getUser();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        List<Post> posts = postService.findPostsByUserId(userId, pageable);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ObjectNode> postDTOS = posts.stream().map(post -> {
            int postId = post.getPostId();
            Boolean liked = likePostService.existsLikedPostByPostIdAndUserId(postId, authenticationFacade.getUser().getUserId());
            PostDTO postDTO = convertToDTO.convertToDTO(post);
            postDTO.setCountLike(likePostService.countLikesByPostId(postId));
            UserDTO userDTO = convertToDTO.convertToDTO(post.getUser());
            postDTO.setUserDTO(userDTO);
            ObjectNode node = mapper.valueToTree(postDTO);
            node.put("liked", liked);
            return node;
        }).toList();
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("success")
                .data(postDTOS)
                .build());

    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            System.out.println(userDTO);
            User user = modelMapper.map(userDTO, User.class);
            System.out.println("TAG!!!" + user.getDepartment());
            user.setUserId(authenticationFacade.getUser().getUserId());
            User updatedUser = userService.update(user);
            responseDTO.setMessage("User updated successfully.");
            responseDTO.setData(updatedUser);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user.");
        }
    }

    @GetMapping("/{userId}/notifications")
    public ResponseEntity<?> getNotifications(@PathVariable int userId,
                                              @RequestParam(defaultValue = "0") Integer pageNum,
                                              @RequestParam(defaultValue = "5") Integer pageSize,
                                              @RequestParam(defaultValue = "createTime") String sortBy) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        List<Notification> notifications = notificationService.findByUserId(userId, pageNum, pageSize, Sort.by(sortBy).descending());
        System.out.println("TAGID: " + notifications.get(0).getUser().getUserId());
        // convert to DTO
        List<?> announcesDTO = notifications.stream()
                .map(announce -> {
                    //nếu là dạng thông báo like
                    if (announce.getType().equals(TypeAnnounce.LIKE)) {
                        NotificationLikePostDTO notificationLikePostDTO = modelMapper.map(announce.getNotificationLikePost(), NotificationLikePostDTO.class);
                        System.out.println("CCC" + notificationLikePostDTO.getNotification().getUser().getUserId());
                        return notificationLikePostDTO;
                    }
                    return null;
                }).toList();
        return ResponseEntity.ok().body(ResponseDTO.builder()
                .message("success")
                .data(announcesDTO)
                .build());
    }

    // search user by name
    @GetMapping("/search")
    public ResponseEntity<?> searchUserByName(@RequestParam String name,
                                              @RequestParam(defaultValue = "0") Integer pageNum,
                                              @RequestParam(defaultValue = "5") Integer pageSize,
                                              @RequestParam(defaultValue = "") String sortBy) {
        List<User> users = userService.searchUserByName(name, pageNum, pageSize, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        List<ObjectNode> userDTOs = users.stream().map(u -> {
            UserDTO userDTO = mapper.convertValue(u, UserDTO.class);
            ObjectNode node = mapper.valueToTree(u);
            System.out.println("TAGREST: " + u.getUserId());
            node.put("isFollowed", followService.existsFollowBySourceIdAndTargetId(authenticationFacade.getUser().getUserId(), userDTO.getUserId()));
            return node;
        }).toList();
        return ResponseEntity.ok().body(ResponseDTO.builder()
                .message("success")
                .data(userDTOs)
                .build());
    }

    // load pending follow
    @GetMapping("/{userId}/pendingFollow")
    public ResponseEntity<?> getPendingFollow(@PathVariable int userId,
                                              @RequestParam(defaultValue = "0") Integer pageNum,
                                              @RequestParam(defaultValue = "5") Integer pageSize,
                                              @RequestParam(defaultValue = "f.followCreateTime") String sortBy) {
        List<User> pendingFollow = userService.findPendingFollowingById(userId, pageNum, pageSize, Sort.by(sortBy));
        return ResponseEntity.ok().body(ResponseDTO.builder()
                .message("success")
                .data(pendingFollow)
                .build());
    }


}
