package com.example.social_media.service;

import com.example.social_media.entity.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface IUserService {
    User findUserById(int userId);
    int findAll(Integer pageNum, Integer pageSize, String sortBy);
}
