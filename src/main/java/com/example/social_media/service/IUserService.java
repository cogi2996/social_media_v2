package com.example.social_media.service;

import com.example.social_media.entity.User;

public interface IUserService {
    User findUserById(int userId);
}
