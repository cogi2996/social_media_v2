package com.example.social_media.entity;

import jakarta.persistence.*;

@Entity
@Table(name="GroupMember")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private int groupId;
    @Column(name="user_id")
    private String userId;
    @Column(name="permission")
    private int permission;

}
