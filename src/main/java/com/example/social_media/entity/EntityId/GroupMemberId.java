package com.example.social_media.entity.EntityId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupMemberId {
    @Column(name="group_id")
    private int groupId;
    @Column(name="user_id")
    private int userId;
}
