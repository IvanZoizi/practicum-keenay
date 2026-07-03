package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.auth.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;

@Tag(name = "Auth Endpoints")
@RequestMapping("/api/v1/auth")
public interface AuthController {
    ResponseEntity<String> registerStudent(@Valid @RequestBody RegisterStudentDTO registerStudentDTO);
    ResponseEntity<String> registerTeacher(@Valid @RequestBody RegisterTeacherDTO registerTeacherDTO);
    ResponseEntity<String> registerAdmin(@Valid @RequestBody RegisterAdminDTO registerAdminDTO);
    ResponseEntity<JwtAutorizeToken> singIn(@Valid @RequestBody LoginDTO loginDTO) throws AuthenticationException;
    ResponseEntity<JwtAutorizeToken> refreshToken(@Valid @RequestBody RefreshTokenDTO loginDTO);
}
