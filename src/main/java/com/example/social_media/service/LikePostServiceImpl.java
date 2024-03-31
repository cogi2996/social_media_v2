package com.example.social_media.service;

import com.example.social_media.dao.LikePostRepository;
import com.example.social_media.entity.EntityId.LikePostId;
import com.example.social_media.entity.LikePost;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikePostServiceImpl implements LikePostService{
    private final LikePostRepository likePostRepository;
    @Override
    public LikePost save(LikePost likePost) {
        return likePostRepository.save(likePost);
    }

    @Override
    public void deleteById(LikePostId likePostId) {
        likePostRepository.deleteById(likePostId);

    }

    @Override
    public Boolean existsLikedPostByPostIdAndUserId(int postId, int userId) {
        return likePostRepository.existsLikedPostByPostIdAndUserId(postId, userId);
    }

    @Override
    public Integer countLikesByPostId(int postId) {
        return likePostRepository.countLikesByPostId(postId);
    }
}
