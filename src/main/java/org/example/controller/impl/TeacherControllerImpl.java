package org.example.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.TeacherController;
import org.example.dto.teachers.TeacherRequestDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.security.CustomUserDetail;
import org.example.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TeacherControllerImpl implements TeacherController {

    private final TeacherService teacherService;

    @Override
    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeachers(@Valid @AuthenticationPrincipal CustomUserDetail userDetail,
                                                             @Valid @RequestBody TeacherRequestDTO teacherRequestDTO,
                                                             @Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(teacherService.updateTeachers(userDetail, teacherRequestDTO, id));
    }
}
