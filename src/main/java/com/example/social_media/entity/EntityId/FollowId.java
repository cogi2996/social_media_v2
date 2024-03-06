package com.example.social_media.entity.EntityId;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FollowId  {
    @Column(name = "source_id")
    private int sourceId;

    @Column(name = "target_id")
    private int targetId;

    // Constructors, getters, setters

}
