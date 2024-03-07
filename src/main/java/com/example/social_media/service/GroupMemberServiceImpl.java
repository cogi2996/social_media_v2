package com.example.social_media.service;

import com.example.social_media.dao.GroupMemberRepository;
import com.example.social_media.entity.GroupMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService{
    private final GroupMemberRepository groupMemberRepository;
    @Override
    public void save(GroupMember groupMember) {
        groupMemberRepository.save(groupMember);
    }

    @Override
    public void saveAndFlush(GroupMember groupMember) {
        groupMemberRepository.saveAndFlush(groupMember);
    }

    @Override
    public void saveAll(List<GroupMember> groupMembers) {
        groupMemberRepository.saveAll(groupMembers);
    }


}
