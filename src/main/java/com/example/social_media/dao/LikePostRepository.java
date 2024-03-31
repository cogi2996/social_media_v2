package com.example.social_media.dao;

import com.example.social_media.entity.EntityId.LikePostId;
import com.example.social_media.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikePostRepository extends JpaRepository<LikePost, LikePostId> {
    // check user liked this post
    @Query("select count(lp) > 0 from LikePost lp where lp.likePostId.postId = ?1 and lp.likePostId.userId = ?2")
    Boolean existsLikedPostByPostIdAndUserId(int postId, int userId);

    // count number of likes for a post
    @Query("select count(lp) from LikePost lp where lp.likePostId.postId = ?1")
    Integer countLikesByPostId(int postId);
}
