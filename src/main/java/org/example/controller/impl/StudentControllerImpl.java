package org.example.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.StudentController;
import org.example.dto.students.StudentRequestDTO;
import org.example.dto.students.StudentResponseDTO;
import org.example.security.CustomUserDetail;
import org.example.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StudentControllerImpl implements StudentController {
    private final StudentService studentService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@Valid @AuthenticationPrincipal CustomUserDetail userDetail, @Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(studentService.getStudent(userDetail, id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getListStudents(@Valid @AuthenticationPrincipal CustomUserDetail userDetail) {
        return ResponseEntity.ok(studentService.getStudents(userDetail));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<StudentResponseDTO> updateStudent(@Valid @AuthenticationPrincipal CustomUserDetail userDetail, @Valid @RequestBody StudentRequestDTO studentRequestDTO,
                                                            @Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(studentService.updateStudent(userDetail, studentRequestDTO, id));
    }
}
