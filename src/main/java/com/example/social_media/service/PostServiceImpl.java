package com.example.social_media.service;

import com.example.social_media.dao.PostRepository;
import com.example.social_media.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;


//    @Override
//    public Post findPostById(int postId) {
//        return postRepository.findOne(postId);
//    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> findPostsByUserIdAndFollowerIds(int userId, List<Integer> followerIds, Pageable pageable) {
        return postRepository.findPostsByUserIdAndFollowerIds(userId, followerIds, pageable).getContent();
    }

    @Override
    public List<Post> findPostsByUserId(int userId, Pageable pageable) {
        return postRepository.findPostsByUserId(userId, pageable).getContent();
    }

    @Override
    public Post findOne(int postId) {
        return postRepository.findOne(postId);
    }


}
