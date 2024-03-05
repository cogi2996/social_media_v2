package com.example.social_media.service;

import com.example.social_media.entity.Group;

public interface GroupService {
    Group save(Group group) ;
    void deleteGroupByGroupId(int groupId);

}
