package com.example.social_media.dao;

import com.example.social_media.entity.EntityId.GroupMemberId;
import com.example.social_media.entity.GroupMember;
import com.example.social_media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    @Modifying
    @Transactional
    @Query("delete from GroupMember gm where gm.groupMemberId.groupId = :groupId")
    void deleteGroupMemberByGroupId(@Param("groupId") int groupId);
}
