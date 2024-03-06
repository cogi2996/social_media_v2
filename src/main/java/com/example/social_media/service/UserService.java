package com.example.social_media.service;

import com.example.social_media.entity.User;

public interface UserService {
    User findUserById(int userId);
}
