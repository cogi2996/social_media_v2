package com.example.social_media.entity;

import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name="LikePost")
public class LikePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private String userId;
    @Column(name="post_id")
    private int postId;
    @Column(name="like_time")
    private Date likeTime;
}
