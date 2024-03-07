package com.example.social_media.rest;

import com.example.social_media.DTO.GroupDTO;
import com.example.social_media.DTO.UserDTO;
import com.example.social_media.Utils.ConvertToDTO;
import com.example.social_media.Utils.ConvertToEntity;
import com.example.social_media.entity.EntityId.GroupMemberId;
import com.example.social_media.entity.Group;
import com.example.social_media.entity.GroupMember;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.security.Role;
import com.example.social_media.service.GroupMemberService;
import com.example.social_media.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/groups")
@RequiredArgsConstructor
public class GroupRestController {
    private final IAuthenticationFacade authenticationFacade;
    private final GroupService groupService;
    private final ModelMapper modelMapper;
    private final ConvertToEntity convertToEntity;
    private final ConvertToDTO convertToDTO;
    private final GroupMemberService groupMemberService;

//    private final GroupMem


    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> addUserToGroup(@PathVariable int groupId, @RequestBody List<UserDTO> listUsers) {
        Group group = this.groupService.findGroupByGroupId(groupId);
        // Not adminGroup or Role.ADMIN can't add user to group
        if (group.getAdminGroup().getUserId() != authenticationFacade.getUser().getUserId()) {
            return ResponseEntity.badRequest().build();
        }
        List<GroupMember> groupMembers = listUsers.stream()
                .map(user -> new GroupMember()
                        .builder()
                        .groupMemberId(new GroupMemberId(groupId, user.getUserId()))
                        .permission(0)
                        .build()
                    ).collect(Collectors.toList());
        groupMemberService.saveAll(groupMembers);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable int groupId) {
        Group group = this.groupService.findGroupByGroupId(groupId);
        System.out.println(group);
        return ResponseEntity.ok(convertToDTO.convertToDTO(group));
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        // convert DTO to entity
        Group newGroup = convertToEntity.convertToEntity(groupDTO);
        newGroup.setCreateTime(new Date(System.currentTimeMillis()));
        // set Admin for group
        newGroup.setAdminGroup(authenticationFacade.getUser());
        // save group
        this.groupService.saveAndFlush(newGroup);
        GroupMemberId groupMemberId = new GroupMemberId(newGroup.getGroupId(), authenticationFacade.getUser().getUserId());
        groupMemberService.save(new GroupMember().builder()
                .groupMemberId(groupMemberId)
                .permission(1)
                .build());
        return ResponseEntity.ok(convertToDTO.convertToDTO(newGroup));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable int groupId) {
        Group group = this.groupService.findGroupByGroupId(groupId);
        User admin = group.getAdminGroup();
        // Not adminGroup or Role.ADMIN can't delete group
        if (admin.getUserId() != authenticationFacade.getUser().getUserId() && authenticationFacade.getRole() != Role.ADMIN) {
            return ResponseEntity.badRequest().build();
        }
        this.groupService.deleteGroupByGroupId(groupId);
        return ResponseEntity.ok().build();
    }


}
