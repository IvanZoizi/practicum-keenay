package org.example.service;

import org.example.dto.groups.GroupDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.entity.Groups;

import java.util.List;

public interface GroupService {
    Groups createGroup(GroupDTO groupDTO);
    List<Groups> getGroups();
    Groups getGroupById(Long id);
    TeacherResponseDTO addGroupTeacher(Long teacherId, Long groupId);
    TeacherResponseDTO getTeacher(Long teacherId);
    TeacherResponseDTO deleteGroupTeacher(Long teacherId, Long groupId);
}
