package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.exception.errors.EntityNotFoundException;
import org.example.exception.errors.ValidationException;
import org.example.repository.UsersRepository;
import org.example.security.CustomUserDetail;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl {

    private final UsersRepository usersRepository;

    public CustomUserDetail getUserByLogin(String login) {
        return  new CustomUserDetail(usersRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("The user with this ID was not found.")));
    }
}
