package org.example.mapper;

import org.example.dto.StudentResponseDTO;
import org.example.entity.Students;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public StudentResponseDTO getDto(Students student) {
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
        studentResponseDTO.setId(student.getId());
        studentResponseDTO.setName(student.getName());
        studentResponseDTO.setSurname(student.getSurname());
        studentResponseDTO.setMiddleName(student.getMiddleName());
        return studentResponseDTO;
    }
}
