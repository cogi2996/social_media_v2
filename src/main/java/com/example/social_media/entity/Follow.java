package com.example.social_media.entity;

import com.example.social_media.entity.EntityId.FollowId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Follow")
public class Follow {
    @EmbeddedId
    private FollowId followId;
    @Column(name = "follow_create_time")
    private LocalDateTime followCreateTime;
    @Column(name = "follow_status", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean followStatus;

    public Follow(FollowId followId ){
        this.followId  = followId;
        this.followCreateTime = LocalDateTime.now();
        this.followStatus =false;
    }

}
