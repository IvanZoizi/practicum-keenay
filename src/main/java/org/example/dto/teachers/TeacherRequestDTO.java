package org.example.dto.teachers;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TeacherRequestDTO {
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String middleName;
    @NonNull
    private String email;
}
