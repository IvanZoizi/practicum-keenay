package org.example.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.GroupController;
import org.example.dto.groups.GroupDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.entity.Groups;
import org.example.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GroupsControllerImpl implements GroupController {

    private final GroupService groupService;

    @Override
    @PostMapping
    public ResponseEntity<Groups> createGroup(@Valid @RequestBody GroupDTO group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Groups>> getAllGroups() {
        return ResponseEntity.ok(groupService.getGroups());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Groups> getGroupById(Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @Override
    @PostMapping("/{groupId}/teacher/{teacherId}")
    public ResponseEntity<TeacherResponseDTO> addGroupTeacher(
                                                              @Valid @PathVariable("groupId") Long groupId,
                                                              @Valid @PathVariable("teacherId") Long teacherId) {
        return ResponseEntity.ok(groupService.addGroupTeacher(teacherId, groupId));
    }

    @Override
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<TeacherResponseDTO> getGroupsByTeacher(Long teacherId) {
        return ResponseEntity.ok(groupService.getTeacher(teacherId));
    }

    @Override
    @DeleteMapping("/{groupId}/teacher{teacherId}")
    public ResponseEntity<TeacherResponseDTO> deleteGroupTeacher( @Valid @PathVariable("groupId") Long groupId, @Valid @PathVariable("teacherId") Long teacherId) {
        return ResponseEntity.ok(groupService.deleteGroupTeacher(teacherId, groupId));
    }
}
