package com.example.social_media.dao;

import com.example.social_media.entity.NotificationLikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLikeRepository extends JpaRepository<NotificationLikePost, Integer> {
}
