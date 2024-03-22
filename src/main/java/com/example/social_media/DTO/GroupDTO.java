package com.example.social_media.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
//@Builder
public class GroupDTO {
    private int groupId;
    private String groupName;
    private Date createTime;
    private UserDTO adminGroup;
}
