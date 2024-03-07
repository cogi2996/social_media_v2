package com.example.social_media.service;

import com.example.social_media.entity.Group;

public interface GroupService {
    Group save(Group group) ;
    Group saveAndFlush(Group group);
    void deleteGroupByGroupId(int groupId);
    Group findGroupByGroupId(int groupId);

}
