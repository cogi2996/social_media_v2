package com.example.social_media.dao;

import com.example.social_media.entity.Follow;
import com.example.social_media.entity.EntityId.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}
