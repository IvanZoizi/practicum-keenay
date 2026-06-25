package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class StudentResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
}
