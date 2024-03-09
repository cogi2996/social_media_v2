package com.example.social_media.service;

import com.example.social_media.entity.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface IUserService {
    User findUserById(int userId);
    Page<User> findAll(int pageNum, int pageSize, String sortBy);
}
