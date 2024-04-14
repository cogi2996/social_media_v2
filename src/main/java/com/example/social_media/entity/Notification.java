package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeAnnounce type;

    @Column(name = "create_time")
    @CreationTimestamp
    private ZonedDateTime createTime;

    @Column(name = "status")
    private boolean status;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    //các thông báo dạng like post
    @OneToOne(mappedBy = "notification", cascade = CascadeType.ALL)
    private NotificationLikePost notificationLikePost;

}