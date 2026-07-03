package org.example.service;

import org.example.dto.students.StudentRequestDTO;
import org.example.dto.students.StudentResponseDTO;
import org.example.security.CustomUserDetail;

import java.util.List;

public interface AdminService {
    StudentResponseDTO getStudentByAdmin(CustomUserDetail customUserDetail, Long id);
    List<StudentResponseDTO> getStudentsByAdmin(CustomUserDetail customUserDetail);
    StudentResponseDTO updateStudentByAdmin(CustomUserDetail customUserDetail, StudentRequestDTO studentRequestDTO, Long id);

}
