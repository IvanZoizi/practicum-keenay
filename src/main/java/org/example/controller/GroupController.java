package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.groups.GroupDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.entity.Groups;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Groups Endpoints")
@RequestMapping("/api/v1/groups")
public interface GroupController {
    ResponseEntity<Groups> createGroup(@RequestBody GroupDTO group);
    ResponseEntity<List<Groups>> getAllGroups();
    ResponseEntity<Groups> getGroupById(@PathVariable("id") Long id);
    ResponseEntity<TeacherResponseDTO> addGroupTeacher(@PathVariable("teacherId") Long teacherId, @PathVariable("groupId") Long groupId);
    ResponseEntity<TeacherResponseDTO> getGroupsByTeacher(@PathVariable("teacherId") Long teacherId);
    ResponseEntity<TeacherResponseDTO> deleteGroupTeacher(@PathVariable("teacherId") Long teacherId, @PathVariable("groupId") Long groupId);

}
