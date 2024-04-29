package com.example.social_media.dao;

import com.example.social_media.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select p from Post p where p.postId = ?1")
    Post findOne(int postId);

    @Query("select p from Post p where ( p.user.userId = ?1 or p.user.userId in ?2 ) and p.status = true order by p.postCreateTime desc")
    Page<Post> findPostsByUserIdAndFollowerIds(int userId, List<Integer> followerIds, Pageable pageable);

    // find user post
    @Query("select p from Post p where p.user.userId = ?1 order by p.postCreateTime desc")
    Page<Post> findPostsByUserId(int userId, Pageable pageable);

    // get total post of user
    @Query("select count(p) from Post p where p.user.userId = ?1")
    int countPostsByUserId(int userId);

    //find all post
    @Query("select p from Post p order by p.postCreateTime desc")
    Page<Post> findAllPosts(Pageable pageable);

    //count all post
    @Query("select count(p) from Post p")
    int countAllPosts();
}
