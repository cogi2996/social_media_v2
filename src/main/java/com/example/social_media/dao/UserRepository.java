package com.example.social_media.dao;

import com.example.social_media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    User findUserByUserId( String userId);

}