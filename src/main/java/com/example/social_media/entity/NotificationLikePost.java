package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_like_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// tétel 1
public class NotificationLikePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id")
    private Notification notification;
    // b
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "user_id")
    private User userLiked;

}
