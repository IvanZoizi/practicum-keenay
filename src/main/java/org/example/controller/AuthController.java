package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.example.dto.auth.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;

@Tag(name = "Auth Endpoints")
@RequestMapping("/api/v1/auth")
public interface AuthController {
    ResponseEntity<String> registerAdmin(@Valid @RequestBody RegisterAdminDTO registerAdminDTO);
    ResponseEntity<String> register(@RequestParam("file") MultipartFile file);
    ResponseEntity<JwtAutorizeToken> singIn(@Valid @RequestBody LoginDTO loginDTO) throws AuthenticationException;
    ResponseEntity<JwtAutorizeToken> refreshToken(@Valid @RequestBody RefreshTokenDTO loginDTO);
    ResponseEntity<String> acceptRegister(@PathParam("token") String token);
    ResponseEntity<String> refreshAcceptRegister(@Valid @RequestBody LoginDTO loginDTO) throws AuthenticationException;
}
