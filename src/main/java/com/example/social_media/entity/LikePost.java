package com.example.social_media.entity;

import com.example.social_media.entity.EntityId.LikePostId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@Entity     
@Table(name = "likepost")
public class LikePost {
    @EmbeddedId
    private LikePostId likePostId;
    @Column(name="like_time")
    @CreationTimestamp
    private Instant likeTime;
}
