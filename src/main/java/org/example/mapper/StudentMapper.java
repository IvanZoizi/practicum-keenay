package org.example.mapper;

import org.example.dto.students.StudentResponseDTO;
import org.example.dto.teachers.TeacherResponseDTO;
import org.example.entity.Students;
import org.example.entity.Teachers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "surname", target = "surname"),
            @Mapping(source = "middleName", target = "middleName")
    })
    StudentResponseDTO getDTO(Students student);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "surname", target = "surname"),
            @Mapping(source = "middleName", target = "middleName"),
            @Mapping(source = "groups", target = "groupsList")
    })
    TeacherResponseDTO getDTO(Teachers teachers);
}