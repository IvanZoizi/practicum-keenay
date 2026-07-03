package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.teachers.TeacherRequestDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.security.CustomUserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Teachers Endpoints")
@RequestMapping("/api/v1/teachers")
public interface TeacherController {
    ResponseEntity<List<TeacherResponseDTO>> getAllTeachers();
    ResponseEntity<TeacherResponseDTO> updateTeachers(@AuthenticationPrincipal CustomUserDetail userDetail,
                                                      @RequestBody TeacherRequestDTO teacherRequestDTO,
                                                      @PathVariable Long id);

}
