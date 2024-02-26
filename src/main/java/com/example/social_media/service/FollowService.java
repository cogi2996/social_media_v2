package com.example.social_media.service;

import com.example.social_media.entity.Follow;
import com.example.social_media.entity.EntityId.FollowId;

public interface FollowService {
        Follow save(Follow follow);
        void deleteFollow(FollowId follow);
        void acceptFollow(FollowId followId);
}
