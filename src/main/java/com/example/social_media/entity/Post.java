package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int postId;

    @Column(name = "post_text")
    private String postText;
    @Column(name = "post_create_time")
    @CreationTimestamp
    private LocalDateTime postCreateTime;
    @Column(name = "post_image")// xin chào
    private String postImage;
    @Column(name="status",columnDefinition = "boolean default true")
    private boolean status;
    // author 's post ( private ) //
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group postGroup;


    // thông bảo like của bài viết
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<NotificationLikePost> notificationLikePosts;

}

