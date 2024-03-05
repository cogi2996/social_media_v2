package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "\"Group\"")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private int groupId;
    @Column(name="group_name")
    private String groupName;
    @Column(name= "create_time")
    private Date createTime;

    @ManyToOne
    @JoinColumn(name="create_id",referencedColumnName = "user_id")
    private User adminGroup;

    // member in group
    @ManyToMany(mappedBy = "memberGroups")
    private List<User> GroupMembers;

    // posts'group
    @OneToMany(mappedBy = "postGroup")
    private List<Post> groupPosts;

}
