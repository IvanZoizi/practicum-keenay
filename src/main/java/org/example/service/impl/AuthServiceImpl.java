package org.example.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.enums.Roles;
import org.example.dto.auth.JwtAutorizeToken;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.RegisterDTO;
import org.example.entity.Groups;
import org.example.entity.Students;
import org.example.entity.Teachers;
import org.example.entity.Users;
import org.example.exception.errors.EntityNotFoundException;
import org.example.exception.errors.ValidationException;
import org.example.repository.GroupsRepository;
import org.example.repository.StudentRepository;
import org.example.repository.TeachersRepository;
import org.example.repository.UsersRepository;
import org.example.security.jwt.JwtService;
import org.example.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final GroupsRepository groupsRepository;
    private final StudentRepository studentRepository;
    private final TeachersRepository teachersRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String register(RegisterDTO registerDto) {
        Groups group = null;
        if (!registerDto.getRole().equals(Roles.ADMIN)) {
            group = groupsRepository.findById(registerDto.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("The group with this ID was not found."));
        }
        if (usersRepository.findByLogin(registerDto.getLogin()).isPresent()) {
            throw new ValidationException("The login is busy.");
        }
        if (registerDto.getPassword().length() < 8) {
            throw new ValidationException("The password is too small.");
        }
        if (registerDto.getPassword().length() > 20) {
            throw new ValidationException("The password is too big.");
        }
        Users user = new Users();
        user.setName(registerDto.getName());
        user.setSurname(registerDto.getSurname());
        user.setMiddleName(registerDto.getMiddleName());
        user.setEmail(registerDto.getEmail());
        user.setLogin(registerDto.getLogin());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user = usersRepository.save(user);
        if (registerDto.getRole() == Roles.STUDENT) {
            Students student = new Students();
            student.setGroup(group);
            student.setUser(user);
            studentRepository.save(student);
        } else if (registerDto.getRole() == Roles.TEACHER) {
            Teachers teachers = new Teachers();
            teachers.setUser(user);
            teachers.setGroups(List.of(group));
            teachersRepository.save(teachers);
        }
        return "success";
    }

    @Override
    public JwtAutorizeToken singIn(LoginDTO loginDTO) throws AuthenticationException {
        Users user = usersRepository.findByLogin(loginDTO.getLogin())
                .orElseThrow(() -> new EntityNotFoundException("The user with this ID was not found."));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid password.");
        }

        return jwtService.generateAuthToken(user.getLogin());
    }

    @Override
    public Users getUserByToken(String login) {
        return null;
    }
}
