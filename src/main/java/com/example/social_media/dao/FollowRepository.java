package com.example.social_media.dao;

import com.example.social_media.entity.Follow;
import com.example.social_media.entity.EntityId.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    @Meta(comment = "Count the number of followers for a user")
    Integer countByFollowId_TargetIdAndFollowStatus(Integer followedUserId, Boolean followStatus);

    @Meta(comment = "Count the number of users that a user is following")
    Integer countByFollowId_SourceIdAndFollowStatus(Integer userId, Boolean followStatus);

    @Meta(comment = "Check if a user is following another user")
    @Query("SELECT CASE WHEN COUNT(f) > 0 and f.followStatus = true THEN 1 WHEN COUNT(f) > 0 and f.followStatus = false THEN 2 ELSE 0 END FROM Follow f WHERE f.followId.sourceId = ?1 AND f.followId.targetId = ?2 ")
    Integer existsFollowBySourceIdAndTargetId(Integer curentUserId, int followerId);




}
