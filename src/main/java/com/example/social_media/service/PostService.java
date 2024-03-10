package com.example.social_media.service;

import com.example.social_media.entity.Post;

public interface PostService {
    Post findPostById(int postId);
    Post createPost(Post post);
}
