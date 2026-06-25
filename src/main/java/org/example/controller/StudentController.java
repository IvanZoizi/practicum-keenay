package org.example.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.StudentRequestDTO;
import org.example.dto.StudentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Students Endpoints")
@RequestMapping("/api/v1/student")
public interface StudentController {
    ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO studentRequestDTO);
    ResponseEntity<StudentResponseDTO> getStudent(@PathVariable("id") Long id);
    ResponseEntity<List<StudentResponseDTO>> getListStudents();
    ResponseEntity<String> deleteStudent(@PathVariable("id") Long id);
}
