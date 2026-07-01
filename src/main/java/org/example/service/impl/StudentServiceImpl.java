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

    @Override
    public StudentResponseDTO getStudent(CustomUserDetail customUserDetail, Long id) {
        Users user =  customUserDetail.getUser();
        Students target = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        if (user.getRole().equals(Roles.STUDENT)) {
            Students source = studentRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
            if (!permissionService.checkPermissionStudentViewing(source, target)) {
                throw new AccessDeniedException("Not enough rights");
            }
            return mapper.getDTO(target);
        } else if (user.getRole().equals(Roles.TEACHER)) {
            Teachers source = teachersRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
            if (!permissionService.checkPermissionTeacher(source, target)) {
                throw new AccessDeniedException("Not enough rights");
            }
            return mapper.getDTO(target);
        }
        return mapper.getDTO(target);


    }

    @Override
    @Transactional
    public List<StudentResponseDTO> getStudents(CustomUserDetail customUserDetail) {
        Users user =  customUserDetail.getUser();
        if (user.getRole().equals(Roles.STUDENT)) {
            Students source = studentRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
            return studentRepository.findAllByGroup_Id(source.getGroup().getId())
                    .map(mapper::getDTO)
                    .toList();
        } else if (user.getRole().equals(Roles.TEACHER)) {
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
        return studentRepository.findAll().stream()
                .map(mapper::getDTO)
                .toList();
    }

    private void updateUser(Users user, StudentRequestDTO studentRequestDTO) {
        user.setName(studentRequestDTO.getName());
        user.setMiddleName(studentRequestDTO.getMiddleName());
        user.setSurname(studentRequestDTO.getSurname());
        if (!EmailValidValidator.isValidSimple(studentRequestDTO.getEmail())) {
            throw new ValidationException("The mail is incorrect.");
        }
        user.setEmail(studentRequestDTO.getEmail());
        System.out.println(user);
        usersRepository.save(user);
    }

    @Override
    public StudentResponseDTO updateStudent(CustomUserDetail customUserDetail, StudentRequestDTO studentRequestDTO,
                                            Long id) {
        Users user =  customUserDetail.getUser();
        if (user.getRole().equals(Roles.STUDENT)) {
            if (id != user.getId()) {
                throw new AccessDeniedException("Not enough rights.");
            }
            this.updateUser(user, studentRequestDTO);
            return mapper.getDTO(studentRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found.")));
        } else if (user.getRole().equals(Roles.TEACHER)) {
            Teachers source = teachersRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
            Students target = studentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
            if (!permissionService.checkPermissionTeacher(source, target)) {
                throw new AccessDeniedException("Not enough rights.");
            }
            user = usersRepository.findById(target.getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
            this.updateUser(user, studentRequestDTO);
            return mapper.getDTO(studentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found.")));
        }
        Students target = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        user = usersRepository.findById(target.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found."));
        this.updateUser(user, studentRequestDTO);
        return mapper.getDTO(studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The student with this ID was not found.")));

    }
}
