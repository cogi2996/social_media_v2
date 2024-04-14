package com.example.social_media.dao;

import com.example.social_media.entity.Notification;
import com.example.social_media.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId")
    List<Notification> findByUserId(int userId,Pageable pageable);

}