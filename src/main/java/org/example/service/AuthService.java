package org.example.service;


import org.example.dto.auth.*;
import org.example.entity.Users;

import javax.naming.AuthenticationException;

public interface AuthService {
    String registerStudent(RegisterStudentDTO registerDto);
    String registerTeacher(RegisterTeacherDTO registerTeacherDTO);
    String registerAdmin(RegisterAdminDTO registerAdminDTO);
    JwtAutorizeToken singIn(LoginDTO loginDTO) throws AuthenticationException;
    JwtAutorizeToken refreshToken(RefreshTokenDTO refreshTokenDTO);
}
