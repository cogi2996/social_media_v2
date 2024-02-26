package com.example.social_media.entity.EntityId;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class LikePostId {
    @Column(name="user_id")
    private String userId;
    @Column(name="post_id")
    private int postId;

}
