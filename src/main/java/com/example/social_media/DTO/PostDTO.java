package com.example.social_media.DTO;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
public class PostDTO {
    private int postId;
    private int userId;
    private int authorId;
    private String postText;
    private Date postCreateTime;
    private String postImage;
    private UserDTO userDTO;
}
