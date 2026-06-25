package org.example.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.StudentController;
import org.example.dto.StudentRequestDTO;
import org.example.dto.StudentResponseDTO;
import org.example.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StudentControllerImpl implements StudentController {
    private final StudentService studentService;

    @Override
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(StudentRequestDTO studentRequestDTO) {
        return ResponseEntity.ok(studentService.createStudent(studentRequestDTO));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getListStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("success");
    }
}
