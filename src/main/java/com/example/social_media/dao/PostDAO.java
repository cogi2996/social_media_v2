package com.example.social_media.dao;

import com.example.social_media.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDAO extends JpaRepository<Post,Integer> {
}
