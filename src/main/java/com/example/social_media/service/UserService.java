package com.example.social_media.service;

import com.example.social_media.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface UserService {
    User findUserById(int userId);

    Page<User> findAll(int pageNum, int pageSize, String sortBy);

    List<User> findFollowersByUserId(int userid, int pageNum, int pageSize, String sortBy);

    List<User> findFollowingUserByUserId(int userId, int pageNum, int pageSize, String sortBy);

    List<User> findPeopleNotFollowedByUserId(int userId, int pageNum, int pageSize);

    User update(User user);

    List<User> searchUserByName(String name, int pageNum, int pageSize, Sort sort);

    List<User> findPendingFollowingById(int userId, int pageNum, int pageSize, String sortBy);

    void delete(int userId);
}
