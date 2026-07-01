package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.auth.JwtAutorizeToken;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.RegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;

@Tag(name = "Auth Endpoints")
@RequestMapping("/api/v1/auth")
public interface AuthController {
    ResponseEntity<String> register(RegisterDTO registerDTO);
    ResponseEntity<JwtAutorizeToken> singIn(LoginDTO loginDTO) throws AuthenticationException;
}
