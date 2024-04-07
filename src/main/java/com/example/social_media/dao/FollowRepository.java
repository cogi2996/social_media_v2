package com.example.social_media.dao;

import com.example.social_media.entity.Follow;
import com.example.social_media.entity.EntityId.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    @Meta(comment = "Count the number of followers for a user")
    Integer countByFollowId_TargetIdAndFollowStatus(Integer followedUserId, Boolean followStatus);

    @Meta(comment = "Count the number of users that a user is following")
    Integer countByFollowId_SourceIdAndFollowStatus(Integer userId, Boolean followStatus);



}
