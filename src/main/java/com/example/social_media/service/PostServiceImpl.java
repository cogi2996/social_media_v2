package com.example.social_media.service;

import com.example.social_media.dao.PostRepository;
import com.example.social_media.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;


    @Override
    public Post findPostById(int postId) {
        return postRepository.findPostByPostId(postId);
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}
