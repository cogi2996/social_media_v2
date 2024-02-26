package com.example.social_media.entity;

import com.example.social_media.entity.EntityId.LikePostId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "likepost")
public class LikePost {
    @EmbeddedId
    private LikePostId likePostId;
    @Column(name="like_time")
    private LocalDateTime likeTime;
}
