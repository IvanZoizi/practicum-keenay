package org.example.dto.students;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.example.validation.EmailValid;


@Data
@NoArgsConstructor
public class StudentRequestDTO {
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String middleName;
    @NonNull
    @EmailValid
    private String  email;
}
