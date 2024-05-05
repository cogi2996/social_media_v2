package com.example.social_media.DTO;

import com.example.social_media.security.ValidPassword;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePassRequestDTO {
    @ValidPassword
    String oldPassword;
    @ValidPassword
    String newPassword;
}


