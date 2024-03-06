package com.example.social_media.DTO;

import com.example.social_media.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String email;
    private String password;
    private Role role;
    private UserDTO userDTO;

}
