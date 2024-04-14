package com.example.social_media.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLikePostDTO {
    private int id;
    private NotificationDTO notification;
    private PostDTO post;
    private UserDTO userLiked;

}
