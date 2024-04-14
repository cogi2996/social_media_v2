package com.example.social_media.DTO;

import com.example.social_media.entity.TypeAnnounce;
import lombok.Data;

import java.time.Instant;
@Data
public class NotificationDTO {
    private TypeAnnounce type;
    private Instant createTime;
    private boolean status;
    private UserDTO user;
}
