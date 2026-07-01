package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.groups.GroupDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.entity.Groups;
import org.example.entity.Teachers;
import org.example.exception.errors.EntityNotFoundException;
import org.example.exception.errors.ValidationException;
import org.example.mapper.StudentMapper;
import org.example.repository.GroupsRepository;
import org.example.repository.TeachersRepository;
import org.example.service.GroupService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupsRepository groupsRepository;
    private final TeachersRepository teachersRepository;
    private final StudentMapper studentMapper;

    @Override
    public Groups createGroup(GroupDTO groupDTO) {
        Groups group = new Groups();
        group.setTitle(groupDTO.getTitle());
        group.setCourse(groupDTO.getCourse());
        return groupsRepository.save(group);
    }

    @Override
    public List<Groups> getGroups() {
        return groupsRepository.findAll();
    }

    @Override
    public Groups getGroupById(Long id) {
        return groupsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("The group with this ID was not found."));
    }

    @Override
    public TeacherResponseDTO addGroupTeacher(Long teacherId, Long groupId) {
        Teachers teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("The teacher with this ID was not found."));
        Groups groupTarget = groupsRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("The group with this ID was not found."));
        List<Groups> groupsList = teacher.getGroups();
        for (Groups group : groupsList) {
            if (group.getId().equals(groupTarget.getId())) {
                throw  new ValidationException("The group has already been added");
            }
        }
        groupsList.add(groupTarget);
        teacher.setGroups(groupsList);
        return studentMapper.getDTO(teachersRepository.save(teacher));
    }

    @Override
    public TeacherResponseDTO getTeacher(Long teacherId) {
        Teachers teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("The teacher with this ID was not found."));
        return studentMapper.getDTO(teacher);
    }

    @Override
    public TeacherResponseDTO deleteGroupTeacher(Long teacherId, Long groupId) {
        Teachers teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("The teacher with this ID was not found."));
        Groups groupTarget = groupsRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("The group with this ID was not found."));
        List<Groups> groupsList = teacher.getGroups();
        for (Groups group : groupsList) {
            if (group.getId().equals(groupTarget.getId())) {
                groupsList.remove(group);
                teacher.setGroups(groupsList);
                return studentMapper.getDTO(teachersRepository.save(teacher));
            }
        }
        throw new ValidationException("This group is not added already");
    }
}
