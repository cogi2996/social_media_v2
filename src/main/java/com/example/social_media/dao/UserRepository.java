package com.example.social_media.dao;

import com.example.social_media.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUserId( int userId);
    @Query("SELECT u.followers FROM User u WHERE u.userId = :userId")
    List<User> findFollowersByUserId(@Param("userId") int userId);


}
