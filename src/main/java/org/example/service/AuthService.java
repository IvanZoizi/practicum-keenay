package org.example.service;


import org.example.dto.auth.*;
import org.example.entity.Users;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.util.List;

public interface AuthService {
    String registerAdmin(RegisterAdminDTO registerAdminDTO);
    JwtAutorizeToken singIn(LoginDTO loginDTO) throws AuthenticationException;
    JwtAutorizeToken refreshToken(RefreshTokenDTO refreshTokenDTO);
    String register(MultipartFile file);
    String accept(String token);
    String refreshAcceptToken(LoginDTO loginDTO) throws AuthenticationException;
}
