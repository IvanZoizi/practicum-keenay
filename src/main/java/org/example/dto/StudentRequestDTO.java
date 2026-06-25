package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


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
    private String group;
    @NonNull
//    @Pattern(
//            regexp = "^\\+?[0-9]{10,15}$",
//            message = "Некорректный номер телефона"
//    )
    private String phone;
}
