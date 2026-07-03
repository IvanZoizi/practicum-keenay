package org.example.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.AuthController;
import org.example.dto.auth.*;
import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(RegisterStudentDTO registerStudentDTO) {
        return ResponseEntity.ok(authService.registerStudent(registerStudentDTO));
    }

    @Override
    @PostMapping("/register/teacher")
    public ResponseEntity<String> registerTeacher(RegisterTeacherDTO registerTeacherDTO) {
        return ResponseEntity.ok(authService.registerTeacher(registerTeacherDTO));
    }

    @Override
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(RegisterAdminDTO registerAdminDTO) {
        return ResponseEntity.ok(authService.registerAdmin(registerAdminDTO));
    }

    @Override
    @PostMapping("/sign/in")
    public ResponseEntity<JwtAutorizeToken> singIn(@Valid @RequestBody LoginDTO loginDTO) throws AuthenticationException {
        return ResponseEntity.ok(authService.singIn(loginDTO));
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<JwtAutorizeToken> refreshToken(RefreshTokenDTO loginDTO) {
        return ResponseEntity.ok(authService.refreshToken(loginDTO));
    }
}
