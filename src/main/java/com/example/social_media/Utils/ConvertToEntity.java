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
public class ConvertToEntity {
    private final ModelMapper modelMapper;
    public  User convertToEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    public Post convertToEntity(PostDTO postDTO){
        return modelMapper.map(postDTO, Post.class);
    }

}
