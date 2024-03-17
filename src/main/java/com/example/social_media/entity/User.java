package com.example.social_media.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;
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
//    @JsonIgnore
//    @Column(name="role")
//    private int role;


    // following
    @ManyToMany
    @JsonIgnore
    @JoinTable(name="Follow", joinColumns = @JoinColumn(name="source_id"),inverseJoinColumns = @JoinColumn(name = "target_id"))
    private List<User> followingUsers;

    // follower
    @ManyToMany(mappedBy = "followingUsers")
    @JsonIgnore
    private List<User> followers;

    // user'post
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    // groups which user created
    @OneToMany(mappedBy = "adminGroup")
    @JsonIgnore
    private List<Group> groupCreates;

    //group which user joined
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name="GroupMember",joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="group_id"))
    private List<Group> memberGroups;

    // các bài viết mà user like

    // account của user
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name="account_id")
    private Account account;




}
