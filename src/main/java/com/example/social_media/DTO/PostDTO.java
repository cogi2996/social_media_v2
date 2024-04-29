package com.example.social_media.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class PostDTO {
    private int postId;
    private String postText;
    private LocalDateTime postCreateTime;
    private String postImage;
    private int countLike;
    private UserDTO userDTO;

}
