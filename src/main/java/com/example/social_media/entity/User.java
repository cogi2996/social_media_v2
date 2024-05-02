package com.example.social_media.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
@Data
public class User implements Serializable {
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
    @Column(name="gender")
    private int gender;
    @Column(name="avatar")
    private String avatar;
    @Column(name="dob")
    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    // following
    @ManyToMany
    @JsonIgnore
    @JoinTable(name="Follow", joinColumns = @JoinColumn(name="source_id"),inverseJoinColumns = @JoinColumn(name = "target_id"))
    private List<User> followingUsers;

    // follower
    @ManyToMany(mappedBy = "followingUsers",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> followers;

    // user'post
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    // các thông báo của user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notification> notifications;

    //các thông báo like được user kích hoạt
    @OneToMany(mappedBy = "userLiked", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<NotificationLikePost> notificationLikePosts;
    // account của user
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name="account_id")
    private Account account;


    @JsonIgnore
    public String getFullName(){
        return this.lastName + " " + this.midName + " " + this.firstName;
    }




}
