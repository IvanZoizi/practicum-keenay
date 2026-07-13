package org.example.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.*;
import org.example.dto.enums.Roles;
import org.example.dto.events.EmailEvent;
import org.example.entity.*;
import org.example.exception.errors.*;
import org.example.repository.*;
import org.example.security.jwt.JwtService;
import org.example.service.AuthService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final GroupsRepository groupsRepository;
    private final StudentRepository studentRepository;
    private final TeachersRepository teachersRepository;
    private final JwtService jwtService;
    private final ParseFileService parseFileService;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final KafkaTemplate<String, EmailEvent> kafkaTemplateEmail;

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

    private EmailEvent createTokenUser(Users user) {

        ConfirmationToken confirmationToken = new ConfirmationToken();

        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime date = LocalDateTime.now().plusDays(7);

        confirmationToken.setUser(user);
        confirmationToken.setToken(tokenValue);
        confirmationToken.setExpiresAt(date);
        confirmationToken.setIsShipped(false);

        EmailEvent emailEvent = new EmailEvent();
        emailEvent.setIdToken(confirmationTokenRepository.save(confirmationToken).getId());
        emailEvent.setEmail(usersRepository.findEmailByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User is not found")));

        return emailEvent;
    }

    private Users createUser(String login, String password, Roles role) {
        Users user = new Users();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        if (role.equals(Roles.ADMIN)) {
            user.setEnabled(true);
        } else {
            user.setEnabled(false);
        }
        return usersRepository.save(user);
    }

    private Users registerStudent(RegisterCSVDTO registerDto) {
        Groups group = groupsRepository.findByTitle(registerDto.getGroupTitle())
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
        return user;
    }

    private Users registerTeacher(RegisterCSVDTO registerTeacherDTO) {
        List<Groups> groupsList = new ArrayList<>();
        groupsList.add(
                groupsRepository.findByTitle(registerTeacherDTO.getGroupTitle())
                        .orElseThrow(() -> new EntityNotFoundException("The group with this ID was not found."))
        );
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
        return user;
    }

    private Boolean checkTokenTime(ConfirmationToken confirmationToken) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(confirmationToken.getExpiresAt())) {
            return false;
        }
        return true;
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

        if (!user.getEnabled()) {
            throw new UserIsNotVerified("The user is not verified");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid password.");
        }

        return jwtService.generateAuthToken(user.getLogin());
    }

    @Override
    public JwtAutorizeToken refreshToken(RefreshTokenDTO refreshTokenDTO) {
        return jwtService.refreshToken(refreshTokenDTO.getRefreshToken());
    }

    @Override
    public String register(MultipartFile file) {
        List<RegisterCSVDTO> registerCSVDTOList = parseFileService.parse(file);
        for (RegisterCSVDTO registerDTO : registerCSVDTOList) {
            Users user;
            if (registerDTO.getRole().equals(Roles.STUDENT)) {
                user = registerStudent(registerDTO);
            } else if (registerDTO.getRole().equals(Roles.TEACHER)) {
                user = registerTeacher(registerDTO);
            } else {
                throw new ValidationException("This role was not found");
            }

            EmailEvent emailEvent = createTokenUser(user);

            kafkaTemplateEmail.send(
                    "email-service",
                    emailEvent
            );
        }
        return "send email";
    }

    @Override
    public String accept(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
        Users user = confirmationToken.getUser();
        if (!checkTokenTime(confirmationToken)) {

            confirmationTokenRepository.deleteById(confirmationToken.getId());

            throw new TokenDyingException("Token is dying");
        } else {
            usersRepository.updateEnable(true, user.getId());
            confirmationTokenRepository.deleteById(confirmationToken.getId());
            return "success";
        }
    }

    @Override
    public String refreshAcceptToken(LoginDTO loginDTO) throws AuthenticationException {
        Users user = usersRepository.findByLogin(loginDTO.getLogin())
                .orElseThrow(() -> new EntityNotFoundException("The user with this ID was not found."));

        if (user.getEnabled()) {
            throw new UserIsNotVerified("The user is verified");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid password.");
        }

        confirmationTokenRepository.deleteByUserId(user.getId());

        EmailEvent emailEvent = createTokenUser(user);

        kafkaTemplateEmail.send(
                "email-service",
                emailEvent
        );

        return "send email";
    }
}
