package org.example.dto.teachers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Groups;

import java.util.List;

@Data
@NoArgsConstructor
public class TeacherResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private List<Groups> groupsList;
}
