package org.example.service;


import org.example.dto.auth.JwtAutorizeToken;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.RegisterDTO;
import org.example.entity.Users;

import javax.naming.AuthenticationException;

public interface AuthService {
    public String register(RegisterDTO registerDto);
    public JwtAutorizeToken singIn(LoginDTO loginDTO) throws AuthenticationException;
    public Users getUserByToken(String login);
}
