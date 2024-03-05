package com.example.social_media.service;

import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserById(int userId) {
        User user = userRepository.findUserByUserId(userId);
        return user;
    }
}
