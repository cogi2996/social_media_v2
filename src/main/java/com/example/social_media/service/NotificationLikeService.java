package com.example.social_media.service;

import com.example.social_media.entity.NotificationLikePost;

public interface NotificationLikeService {
    void save(NotificationLikePost notificationLikePost);
    void delete(NotificationLikePost notificationLikePost);
    NotificationLikePost findByUserLikedId(int userId, int postId);

}
