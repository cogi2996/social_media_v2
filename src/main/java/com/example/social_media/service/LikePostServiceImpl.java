package com.example.social_media.service;

import com.example.social_media.dao.LikePostRepository;
import com.example.social_media.entity.EntityId.LikePostId;
import com.example.social_media.entity.LikePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikePostServiceImpl implements LikePostService{
    @Autowired
    private LikePostRepository likePostRepository;
    @Override
    public LikePost save(LikePost likePost) {
        return likePostRepository.save(likePost);
    }

    @Override
    public void deleteById(LikePostId likePostId) {
        likePostRepository.deleteById(likePostId);

    }
}
