package org.example.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RegisterAdminDTO {
    @NonNull
    private String login;
    @NonNull
    @Size(min = 8, max = 20)
    private String password;
}
