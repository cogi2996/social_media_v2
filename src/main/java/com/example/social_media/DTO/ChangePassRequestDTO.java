package com.example.social_media.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePassRequestDTO {
    String oldPassword;
    String newPassword;
}


