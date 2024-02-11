package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
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
