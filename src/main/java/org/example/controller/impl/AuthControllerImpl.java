package org.example.controller.impl;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.AuthController;
import org.example.dto.auth.*;
import org.example.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;


    @Override
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(RegisterAdminDTO registerAdminDTO) {
        return ResponseEntity.ok(authService.registerAdmin(registerAdminDTO));
    }

    @Override
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(authService.register(file));
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

    @Override
    @GetMapping("/accept")
    public ResponseEntity<String> acceptRegister(@PathParam("token") String token) {
        return ResponseEntity.ok(authService.accept(token));
    }

    @Override
    @PostMapping("/refresh/accept")
    public ResponseEntity<String> refreshAcceptRegister(@Valid @RequestBody LoginDTO loginDTO) throws AuthenticationException {
        return ResponseEntity.ok(authService.refreshAcceptToken(loginDTO));
    }
}
