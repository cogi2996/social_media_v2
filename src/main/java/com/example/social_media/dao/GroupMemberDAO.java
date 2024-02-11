package com.example.social_media.dao;

import com.example.social_media.entity.Group;
import com.example.social_media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberDAO extends JpaRepository<User,Integer> {
}
