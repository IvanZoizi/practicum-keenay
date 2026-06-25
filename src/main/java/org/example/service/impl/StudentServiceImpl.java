package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.StudentRequestDTO;
import org.example.dto.StudentResponseDTO;
import org.example.entity.Students;
import org.example.mapper.Mapper;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final Mapper mapper;


    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {
        Students students = new Students();
        students.setName(studentRequestDTO.getName());
        students.setSurname(studentRequestDTO.getSurname());
        students.setMiddleName(studentRequestDTO.getMiddleName());
        students.setGroup(studentRequestDTO.getGroup());
        students.setPhone(studentRequestDTO.getPhone());
        return mapper.getDto(studentRepository.save(students));
    }

    @Override
    public StudentResponseDTO getStudent(Long id) {
        return mapper.getDto(studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id is incorrect")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAllAsStream()
                .map(mapper::getDto)
                .toList();
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
