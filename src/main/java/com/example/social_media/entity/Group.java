package com.example.social_media.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="Group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private int groupId;
    @Column(name="group_name")
    private String groupName;
    @Column(name= "create_time")
    private Date createTime;
    @Column(name="create_id")
    private String createId;

    @ManyToOne
    @JoinColumn(name="create_id",referencedColumnName = "user_id")
    private User adminGroup;

    // member in group
    @ManyToMany(mappedBy = "memberGroups")
    private List<User> GroupMembers;

}
