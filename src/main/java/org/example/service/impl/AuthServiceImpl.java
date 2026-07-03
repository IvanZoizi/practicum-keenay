package org.example.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.*;
import org.example.dto.enums.Roles;
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
import java.util.ArrayList;
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

    private void checkUserLogin(String login, String password) {
        if (usersRepository.findByLogin(login).isPresent()) {
            throw new ValidationException("The login is busy.");
        }
        if (password.length() < 8) {
            throw new ValidationException("The password is too small.");
        }
        if (password.length() > 20) {
            throw new ValidationException("The password is too big.");
        }
    }

    private Users createUser(String login, String password, Roles role) {
        Users user = new Users();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return usersRepository.save(user);
    }

    @Override
    @Transactional
    public String registerStudent(RegisterStudentDTO registerDto) {
        Groups group = groupsRepository.findById(registerDto.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("The group with this ID was not found."));
        this.checkUserLogin(registerDto.getLogin(), registerDto.getPassword());
        Users user = createUser(registerDto.getLogin(),
                registerDto.getPassword(),
                Roles.STUDENT);
        Students student = new Students();
        student.setName(registerDto.getName());
        student.setSurname(registerDto.getSurname());
        student.setMiddleName(registerDto.getMiddleName());
        student.setEmail(registerDto.getEmail());
        student.setUser(user);
        student.setGroup(group);
        studentRepository.save(student);
        return "success";
    }

    @Override
    @Transactional
    public String registerTeacher(RegisterTeacherDTO registerTeacherDTO) {
        List<Groups> groupsList = new ArrayList<>();
        for (Long idGroup : registerTeacherDTO.getGroupId()) {
            groupsList.add(groupsRepository.findById(idGroup)
                    .orElseThrow(() -> new EntityNotFoundException("The group with this ID was not found.")));
        }
        this.checkUserLogin(registerTeacherDTO.getLogin(), registerTeacherDTO.getPassword());
        Users user = createUser(registerTeacherDTO.getLogin(),
                registerTeacherDTO.getPassword(),
                Roles.TEACHER);
        Teachers teacher = new Teachers();
        teacher.setName(registerTeacherDTO.getName());
        teacher.setSurname(registerTeacherDTO.getSurname());
        teacher.setMiddleName(registerTeacherDTO.getMiddleName());
        teacher.setEmail(registerTeacherDTO.getEmail());
        teacher.setUser(user);
        teacher.setGroups(groupsList);
        teachersRepository.save(teacher);
        return "success";
    }

    @Override
    public String registerAdmin(RegisterAdminDTO registerAdminDTO) {
        this.checkUserLogin(registerAdminDTO.getLogin(), registerAdminDTO.getPassword());
        Users user = createUser(registerAdminDTO.getLogin(),
                registerAdminDTO.getPassword(),
                Roles.ADMIN);
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
    public JwtAutorizeToken refreshToken(RefreshTokenDTO refreshTokenDTO) {
        return jwtService.refreshToken(refreshTokenDTO.getRefreshToken());
    }
}
