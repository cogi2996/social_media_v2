package com.example.social_media.service;

import com.example.social_media.entity.GroupMember;

import java.util.List;

public interface GroupMemberService {
    void save(GroupMember groupMember);
    void saveAndFlush(GroupMember groupMember);
    void saveAll(List<GroupMember> groupMembers);
}
