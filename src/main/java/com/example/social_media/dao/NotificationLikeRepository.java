package com.example.social_media.dao;

import com.example.social_media.entity.NotificationLikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationLikeRepository extends JpaRepository<NotificationLikePost, Integer> {
    // find notify by source id
    @Query("SELECT n FROM NotificationLikePost n WHERE n.userLiked.userId = ?1 and n.post.postId = ?2")
    NotificationLikePost findByUserLikedId(int userId, int postId);


}
