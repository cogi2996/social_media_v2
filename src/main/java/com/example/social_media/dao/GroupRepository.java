package com.example.social_media.dao;

import com.example.social_media.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group,Integer> {
    Group findByGroupId(int groupId);
    void deleteByGroupId(int groupId);
}
