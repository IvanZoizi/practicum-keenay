package org.example.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class JwtAutorizeToken {
    String token;
    String refreshToken;
}
