package com.example.social_media.dao;

import com.example.social_media.entity.EntityId.GroupMemberId;
import com.example.social_media.entity.GroupMember;
import com.example.social_media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
}
