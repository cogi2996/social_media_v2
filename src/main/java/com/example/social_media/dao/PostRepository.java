package com.example.social_media.dao;

import com.example.social_media.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    Post findPostByPostId(int postId);
    @Query("select p from Post p where p.user.userId = ?1 or p.user.userId in ?2 order by p.postCreateTime desc")
    Page<Post> findPostsByUserIdAndFollowerIds(int userId, List<Integer> followerIds, Pageable pageable);



}
