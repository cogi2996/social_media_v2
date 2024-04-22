package com.example.social_media.service;

import com.example.social_media.dao.NotificationLikeRepository;
import com.example.social_media.dao.PostRepository;
import com.example.social_media.entity.NotificationLikePost;
import com.example.social_media.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final NotificationLikeRepository notificationLikeRepository;

    @Override
    public Post save(Post post) {
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

    @Override
    public int countPostsByUserId(int userId) {
        return postRepository.countPostsByUserId(userId);
    }

    @Override
    public Page<Post> findAllPosts(int pageNum, int pageSize, String SortBy) {
        if (SortBy == null)
            return postRepository.findAllPosts(PageRequest.of(pageNum, pageSize));
        return postRepository.findAllPosts(PageRequest.of(pageNum, pageSize, Sort.by(SortBy)));
    }

    @Override
    public void deleteById(int postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public int countAllPosts() {
        return postRepository.countAllPosts();
    }

    @Override
    public void deleteByPostId(int postId) {
        // xoá like post
        postRepository.deleteById(postId);
        // delete notify like post
        notificationLikeRepository.deleteByPostId(postId);
    }


}
