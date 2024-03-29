package com.example.social_media.service;

import com.example.social_media.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Post findPostById(int postId);
    Post createPost(Post post);
    List<Post> findPostsByUserIdAndFollowerIds(int userId, List<Integer> followerIds, Pageable pageable);
}
