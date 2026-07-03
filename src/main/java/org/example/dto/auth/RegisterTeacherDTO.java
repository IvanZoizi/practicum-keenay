package org.example.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.example.dto.enums.Roles;
import org.example.validation.EmailValid;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisterTeacherDTO {
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String middleName;
    @NonNull
    @EmailValid
    private String email;
    @NonNull
    private String login;
    @NonNull
    @Size(min = 8, max = 20)
    private String password;
    @NonNull
    private List<Long> groupId;
}
