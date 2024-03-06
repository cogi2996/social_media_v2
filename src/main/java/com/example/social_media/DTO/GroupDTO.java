package com.example.social_media.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class GroupDTO {
    private int groupId;
    private String groupName;
    private Date createTime;

}
