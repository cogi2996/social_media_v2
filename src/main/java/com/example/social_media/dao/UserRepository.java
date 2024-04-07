package com.example.social_media.dao;

import com.example.social_media.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUserId( int userId);
    //
    @Query("SELECT u FROM User u join Follow f on u.userId = f.followId.sourceId " +
            "and f.followId.targetId= :userId and f.followStatus = true")
    List<User> findFollowersByUserId(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT u FROM User u join Follow f on u.userId = f.followId.targetId and f.followId.sourceId = :userId and f.followStatus = true")
    List<User> findFollowingUserByUserId(int userId,Pageable pageable);


    //peple who are not followed
    @Query("SELECT u FROM User u WHERE u.userId NOT IN (SELECT f.userId FROM User u JOIN u.followingUsers f WHERE u.userId = :userId) and u.userId != :userId")
    List<User> findPeopleNotFollowedByUserId(@Param("userId") int userId, Pageable pageable);


}
