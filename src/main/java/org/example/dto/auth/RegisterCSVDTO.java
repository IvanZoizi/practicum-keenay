package org.example.dto.auth;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.example.dto.enums.Roles;
import org.example.validation.EmailValid;

@Data
@NoArgsConstructor
public class RegisterCSVDTO {
    @CsvBindByName(column = "name")
    @CsvBindByPosition(position = 0)
    @NotBlank(message = "Name is required")
    @NonNull
    private String name;

    @CsvBindByName(column = "surname")
    @CsvBindByPosition(position = 1)
    @NotBlank(message = "Surname is required")
    @NonNull
    private String surname;

    @CsvBindByName(column = "middle_name")
    @CsvBindByPosition(position = 2)
    @NotBlank(message = "Middle name is required")
    @NonNull
    private String middleName;

    @CsvBindByName(column = "role")
    @CsvBindByPosition(position = 3)
    @NotBlank(message = "Role is required")
    @NonNull
    @Pattern(
            regexp = "^(ADMIN|TEACHER|STUDENT)$",
            message = "Role must be one of: ADMIN, TEACHER, STUDENT"
    )
    private Roles role;

    @CsvBindByName(column = "email")
    @CsvBindByPosition(position = 4)
    @NotBlank(message = "Email is required")
    @EmailValid
    @NonNull
    private String email;

    @CsvBindByName(column = "group_title")
    @CsvBindByPosition(position = 5)
    @NotBlank(message = "Group title is required")
    @NonNull
    private String groupTitle;

    @CsvBindByName(column = "login")
    @CsvBindByPosition(position = 6)
    @NotBlank(message = "Login is required")
    @NonNull
    private String login;

    @CsvBindByName(column = "password")
    @CsvBindByPosition(position = 7)
    @NotBlank(message = "Password is required")
    @NonNull
    private String password;

}
