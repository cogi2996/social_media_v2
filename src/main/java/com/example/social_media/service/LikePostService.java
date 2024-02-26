package com.example.social_media.service;

import com.example.social_media.entity.EntityId.LikePostId;
import com.example.social_media.entity.LikePost;

public interface LikePostService {
    LikePost save(LikePost likePost);
    void deleteById(LikePostId likePostId);
}
