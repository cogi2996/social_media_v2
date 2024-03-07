package com.example.social_media.entity;

import com.example.social_media.entity.EntityId.GroupMemberId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name="groupmember")

public class GroupMember {
    @EmbeddedId
    GroupMemberId groupMemberId;
    @Column(name="permission")
    private int permission;

}
