package org.example.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.students.StudentRequestDTO;
import org.example.dto.students.StudentResponseDTO;
import org.example.security.CustomUserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Students Endpoints")
@RequestMapping("/api/v1/student")
public interface StudentController {
    ResponseEntity<StudentResponseDTO> getStudent(@AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable("id") Long id);
    ResponseEntity<List<StudentResponseDTO>> getListStudents(@AuthenticationPrincipal CustomUserDetail userDetail);
    ResponseEntity<StudentResponseDTO> updateStudent(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody StudentRequestDTO studentRequestDTO,
                                                     @PathVariable("id") Long id);
}
