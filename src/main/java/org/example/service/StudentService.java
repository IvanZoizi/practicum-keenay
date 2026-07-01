package org.example.service;

import org.example.dto.students.StudentRequestDTO;
import org.example.dto.students.StudentResponseDTO;
import org.example.security.CustomUserDetail;

import java.util.List;

public interface StudentService {
    StudentResponseDTO getStudent(CustomUserDetail customUserDetail, Long id);
    List<StudentResponseDTO> getStudents(CustomUserDetail customUserDetail);
    StudentResponseDTO updateStudent(CustomUserDetail customUserDetail, StudentRequestDTO studentRequestDTO, Long id);
}
