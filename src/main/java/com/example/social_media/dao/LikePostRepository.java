package com.example.social_media.dao;

import com.example.social_media.entity.EntityId.LikePostId;
import com.example.social_media.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost, LikePostId> {
}
