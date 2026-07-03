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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeachersRepository teachersRepository;
    private final UsersRepository usersRepository;
    private final StudentMapper studentMapper;

    private Teachers updateUser(Teachers teacher, TeacherRequestDTO studentRequestDTO) {
        teacher.setName(studentRequestDTO.getName());
        teacher.setMiddleName(studentRequestDTO.getMiddleName());
        teacher.setSurname(studentRequestDTO.getSurname());
        if (!EmailValidValidator.isValidSimple(studentRequestDTO.getEmail())) {
            throw new ValidationException("The mail is incorrect.");
        }
        teacher.setEmail(studentRequestDTO.getEmail());
        return teachersRepository.save(teacher);
    }

    @Override
    public List<TeacherResponseDTO> getAllTeachers() {
        return teachersRepository.findAll().stream()
                .map(studentMapper::getDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public TeacherResponseDTO updateTeachers(CustomUserDetail userDetail, TeacherRequestDTO teacherRequestDTO,
                                             Long id) {
        Users user = userDetail.getUser();
        if (user.getRole() == Roles.TEACHER) {
            return this.updateTeachersByTeachers(userDetail, teacherRequestDTO, id);
        }
        return this.updateTeachersByAdmin(userDetail, teacherRequestDTO, id);
    }

    @PreAuthorize("hasRole('TEACHER')")
    public TeacherResponseDTO updateTeachersByTeachers(CustomUserDetail userDetail, TeacherRequestDTO teacherRequestDTO,
                                             Long id) {
        Teachers source = teachersRepository.findByUser_Id(userDetail.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("The teacher with this ID was not found."));
        if (id != source.getId()) {
            throw new AccessDeniedException("Not enough rights");
        }
        return studentMapper.getDTO(updateUser(source, teacherRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponseDTO updateTeachersByAdmin(CustomUserDetail userDetail, TeacherRequestDTO teacherRequestDTO, Long id) {
        Teachers teacher = teachersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The user with this ID was not found."));
        return studentMapper.getDTO(updateUser(teacher, teacherRequestDTO));
    }


}
