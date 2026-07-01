package org.example.service;

import org.example.dto.teachers.TeacherRequestDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.security.CustomUserDetail;

import java.util.List;

public interface TeacherService {
    List<TeacherResponseDTO> getAllTeachers();
    TeacherResponseDTO updateTeachers(CustomUserDetail userDetail,
                                       TeacherRequestDTO teacherRequestDTO,
                                      Long id);


}
