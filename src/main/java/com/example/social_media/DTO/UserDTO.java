package com.example.social_media.DTO;

import com.example.social_media.security.Role;
import lombok.Data;

import java.util.Date;
@Data
public class UserDTO {
    private int userId;
    private String phone;
    private Date createDate;
    private Date lastLogin;
    private String firstName;
    private String midName;
    private String lastName;
    private String address;
    private String biography;
    private String major;
    private String department;
    private String text;
    private Role role;
}
