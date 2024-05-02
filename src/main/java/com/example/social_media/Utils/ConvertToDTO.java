package com.example.social_media.Utils;

import com.example.social_media.DTO.PostDTO;
import com.example.social_media.DTO.UserDTO;
import com.example.social_media.entity.Post;
import com.example.social_media.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConvertToDTO {
    private final ModelMapper modelMapper;
    public UserDTO convertToDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }


    public PostDTO convertToDTO(Post post){
        return modelMapper.map(post, PostDTO.class);
    }

}
