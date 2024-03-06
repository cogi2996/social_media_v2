package com.example.social_media.rest;

import com.example.social_media.DTO.GroupDTO;
import com.example.social_media.entity.Group;
import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.security.Role;
import com.example.social_media.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/v1/groups")
@RequiredArgsConstructor
public class GroupRestController {
    private final IAuthenticationFacade authenticationFacade;
    private final GroupService groupService;
    private final ModelMapper modelMapper;
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable int groupId) {
        Group group = this.groupService.findGroupByGroupId(groupId);
        System.out.println(group);
        return ResponseEntity.ok(this.convertToGroupDTO(group));
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        // convert DTO to entity
        Group newGroup = this.convertToGroupEntity(groupDTO);
        newGroup.setCreateTime(new Date(System.currentTimeMillis()));
        // set Admin for group
        newGroup.setAdminGroup(authenticationFacade.getUser());
        // save group
        this.groupService.save(newGroup);
        return ResponseEntity.ok(this.convertToGroupDTO(newGroup));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable int groupId) {
        Group group = this.groupService.findGroupByGroupId(groupId);
        User admin = group.getAdminGroup();
        // Not adminGroup or Role.ADMIN can't delete group
        if(admin.getUserId() != authenticationFacade.getUser().getUserId() && authenticationFacade.getRole() != Role.ADMIN){
            return ResponseEntity.badRequest().build();
        }
        this.groupService.deleteGroupByGroupId(groupId);
        return ResponseEntity.ok().build();
    }

    private Group convertToGroupEntity(GroupDTO groupDTO) {
        return modelMapper.map(groupDTO, Group.class);
    }
    private GroupDTO convertToGroupDTO(Group group) {
        return modelMapper.map(group, GroupDTO.class);
    }


}
