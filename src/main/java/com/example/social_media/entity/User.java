package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private String userId;
    @Column(name="phone")
    private String phone;
    @Column(name="create_date")
    private Date createDate;
    @Column(name="last_login")
    private Date lastLogin;
    @Column(name="first_name")
    private String firstName;
    @Column(name="mid_name")
    private String midName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="address")
    private String address;
    @Column(name="biography")
    private String biography;
    @Column(name="major")
    private String major;
    @Column(name="department")
    private String department;
    @Column(name="role")
    private int role;


    // following
    @ManyToMany
    @JoinTable(name="Follow", joinColumns = @JoinColumn(name="source_id"),inverseJoinColumns = @JoinColumn(name = "target_id"))
    private List<User> followingUsers;

    // follower
    @ManyToMany(mappedBy = "followingUsers")
    private List<User> followers;

    // user'post
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    // groups which user created
    @OneToMany(mappedBy = "adminGroup")
    private List<Group> groupCreates;

    //group which user joined
    @ManyToMany
    @JoinTable(name="GroupMember",joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="group_id"))
    private List<Group> memberGroups;


}
