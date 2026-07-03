package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.enums.Roles;
import org.example.dto.students.StudentRequestDTO;
import org.example.dto.students.StudentResponseDTO;
import org.example.entity.Groups;
import org.example.entity.Students;
import org.example.entity.Teachers;
import org.example.entity.Users;
import org.example.exception.errors.AccessDeniedException;
import org.example.exception.errors.EntityNotFoundException;
import org.example.exception.errors.ValidationException;
import org.example.mapper.StudentMapper;
import org.example.repository.StudentRepository;
import org.example.repository.TeachersRepository;
import org.example.repository.UsersRepository;
import org.example.security.CustomUserDetail;
import org.example.service.PermissionService;
import org.example.service.StudentService;
import org.example.validation.EmailValidValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final UsersRepository usersRepository;
    private final TeachersRepository teachersRepository;
    private final StudentRepository studentRepository;
    private final StudentMapper mapper;
    private final PermissionService permissionService;

    @PreAuthorize("hasRole('TEACHER')")
    private StudentResponseDTO getStudentByTeacher(CustomUserDetail customUserDetail, Long id) {
        Users user =  customUserDetail.getUser();
        Students target = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        Teachers source = teachersRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        if (!permissionService.checkPermissionTeacher(source, target)) {
            throw new AccessDeniedException("Not enough rights");
        }
        return mapper.getDTO(target);
    }

    @PreAuthorize("hasRole('STUDENT')")
    private StudentResponseDTO getStudentByStudent(CustomUserDetail customUserDetail, Long id) {
        Users user =  customUserDetail.getUser();
        Students target = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        Students source = studentRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        if (!permissionService.checkPermissionStudentViewing(source, target)) {
            throw new AccessDeniedException("Not enough rights");
        }
        return mapper.getDTO(target);
    }

    @PreAuthorize("hasRole('ADMIN')")
    private StudentResponseDTO getStudentByAdmin(CustomUserDetail customUserDetail, Long id) {
        Students target = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        return mapper.getDTO(target);
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public StudentResponseDTO getStudent(CustomUserDetail customUserDetail, Long id) {
        Users user =  customUserDetail.getUser();
        if (user.getRole() == Roles.STUDENT)  {
            return this.getStudentByStudent(customUserDetail, id);
        } else if (user.getRole() == Roles.TEACHER) {
            return this.getStudentByTeacher(customUserDetail, id);
        }
        return this.getStudentByAdmin(customUserDetail, id);
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public List<StudentResponseDTO> getStudents(CustomUserDetail customUserDetail) {
        Users user =  customUserDetail.getUser();
        if (user.getRole() == Roles.STUDENT)  {
            return this.getStudentsByStudent(customUserDetail);
        } else if (user.getRole() == Roles.TEACHER) {
            return this.getStudentsByTeacher(customUserDetail);
        }
        return this.getStudentsByAdmin(customUserDetail);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    private List<StudentResponseDTO> getStudentsByStudent(CustomUserDetail customUserDetail) {
        Users user =  customUserDetail.getUser();
        Students source = studentRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        return studentRepository.findAllByGroup_Id(source.getGroup().getId())
                .map(mapper::getDTO)
                .toList();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Transactional
    private List<StudentResponseDTO> getStudentsByTeacher(CustomUserDetail customUserDetail) {
        Users user = customUserDetail.getUser();
        List<Students> studentsList = new ArrayList<>();
        Teachers source = teachersRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The teacher with this ID was not found."));
        for (Groups groups : source.getGroups()) {
            studentsList.addAll(studentRepository.findAllByGroup_Id(groups.getId())
                    .toList());
        }
        return studentsList.stream()
                .map(mapper::getDTO)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    private List<StudentResponseDTO> getStudentsByAdmin(CustomUserDetail customUserDetail) {
        return studentRepository.findAll().stream()
                .map(mapper::getDTO)
                .toList();
    }

    private Students updateUser(Students student, StudentRequestDTO studentRequestDTO) {
        student.setName(studentRequestDTO.getName());
        student.setMiddleName(studentRequestDTO.getMiddleName());
        student.setSurname(studentRequestDTO.getSurname());
        if (!EmailValidValidator.isValidSimple(studentRequestDTO.getEmail())) {
            throw new ValidationException("The mail is incorrect.");
        }
        student.setEmail(studentRequestDTO.getEmail());
        return studentRepository.save(student);
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public StudentResponseDTO updateStudent(CustomUserDetail customUserDetail, StudentRequestDTO studentRequestDTO,
                                            Long id) {
        Users user = customUserDetail.getUser();
        if (user.getRole() == Roles.STUDENT) {
            return this.updateStudentByStudent(customUserDetail, studentRequestDTO, id);
        } else if (user.getRole() == Roles.TEACHER) {
            return this.updateStudentByTeacher(customUserDetail, studentRequestDTO, id);
        }
        return this.updateStudentByAdmin(customUserDetail, studentRequestDTO, id);
    }

    @PreAuthorize("hasRole('STUDENT')")
    private StudentResponseDTO updateStudentByStudent(CustomUserDetail customUserDetail, StudentRequestDTO studentRequestDTO,
                                            Long id) {
        Students student = studentRepository.findByUser_Id(customUserDetail.getUser().getId()).
                orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        if (id != student.getId()) {
            throw new AccessDeniedException("Not enough rights.");
        }
        return mapper.getDTO(this.updateUser(student, studentRequestDTO));


    }

    @PreAuthorize("hasRole('TEACHER')")
    private StudentResponseDTO updateStudentByTeacher(CustomUserDetail customUserDetail, StudentRequestDTO studentRequestDTO, Long id) {
        Users user =  customUserDetail.getUser();
        Teachers source = teachersRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        Students target = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        if (!permissionService.checkPermissionTeacher(source, target)) {
            throw new AccessDeniedException("Not enough rights.");
        }
        return mapper.getDTO(this.updateUser(target, studentRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    private StudentResponseDTO updateStudentByAdmin(CustomUserDetail customUserDetail, StudentRequestDTO studentRequestDTO, Long id) {
        Students target = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        return mapper.getDTO(this.updateUser(target, studentRequestDTO));
    }
}
