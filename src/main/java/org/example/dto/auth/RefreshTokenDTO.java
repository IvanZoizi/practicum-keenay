package org.example.dto.auth;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RefreshTokenDTO {
    @NonNull
    private String refreshToken;
}
