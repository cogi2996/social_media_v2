package com.example.social_media.dao;

import com.example.social_media.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUserId( int userId);

}
