package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.enums.Roles;
import org.example.dto.teachers.TeacherRequestDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.entity.Teachers;
import org.example.entity.Users;
import org.example.exception.errors.AccessDeniedException;
import org.example.exception.errors.EntityNotFoundException;
import org.example.exception.errors.ValidationException;
import org.example.mapper.StudentMapper;
import org.example.repository.GroupsRepository;
import org.example.repository.TeachersRepository;
import org.example.repository.UsersRepository;
import org.example.security.CustomUserDetail;
import org.example.service.TeacherService;
import org.example.validation.EmailValidValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeachersRepository teachersRepository;
    private final UsersRepository usersRepository;
    private final GroupsRepository groupsRepository;
    private final StudentMapper studentMapper;

    private void updateUser(Users user, TeacherRequestDTO studentRequestDTO) {
        user.setName(studentRequestDTO.getName());
        user.setMiddleName(studentRequestDTO.getMiddleName());
        user.setSurname(studentRequestDTO.getSurname());
        if (!EmailValidValidator.isValidSimple(studentRequestDTO.getEmail())) {
            throw new ValidationException("The mail is incorrect.");
        }
        user.setEmail(studentRequestDTO.getEmail());
        usersRepository.save(user);
    }

    @Override
    public List<TeacherResponseDTO> getAllTeachers() {
        return teachersRepository.findAll().stream()
                .map(studentMapper::getDTO)
                .toList();
    }

    @Override
    public TeacherResponseDTO updateTeachers(CustomUserDetail userDetail, TeacherRequestDTO teacherRequestDTO,
                                             Long id) {
        Users user = userDetail.getUser();
        if (user.getRole().equals(Roles.TEACHER)) {
            Teachers source = teachersRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("The teacher with this ID was not found."));
            if (id != source.getId()) {
                throw new AccessDeniedException("Not enough rights");
            }
            updateUser(user, teacherRequestDTO);
            return studentMapper.getDTO(teachersRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("The user with this ID was not found.")));
        }
        user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The user with this ID was not found."));
        updateUser(user, teacherRequestDTO);
        return studentMapper.getDTO(teachersRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The user with this ID was not found.")));
    }


}
