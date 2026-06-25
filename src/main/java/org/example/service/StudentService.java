package org.example.service;

import org.example.dto.StudentRequestDTO;
import org.example.dto.StudentResponseDTO;

import java.util.List;

public interface StudentService {
    StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO);
    StudentResponseDTO getStudent(Long id);
    List<StudentResponseDTO> getAllStudents();
    void deleteStudent(Long id);
}
