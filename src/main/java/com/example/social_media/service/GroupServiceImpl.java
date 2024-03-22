package com.example.social_media.service;

import com.example.social_media.dao.GroupMemberRepository;
import com.example.social_media.dao.GroupRepository;
import com.example.social_media.entity.Group;
import com.example.social_media.entity.GroupMember;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group saveAndFlush(Group group) {
        return groupRepository.saveAndFlush(group);
    }

    @Override
    public void deleteGroupById(int groupId) {
        groupMemberRepository.deleteGroupMemberByGroupId(groupId);
        groupRepository.deleteById(groupId);
    }

    @Override
    public Group findGroupByGroupId(int groupId) {
        return groupRepository.findByGroupId(groupId);
    }
}
