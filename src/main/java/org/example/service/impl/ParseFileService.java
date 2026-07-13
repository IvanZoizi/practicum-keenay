package org.example.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.RegisterCSVDTO;
import org.example.entity.Users;
import org.example.exception.errors.*;
import org.example.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseFileService {

    private final UsersRepository usersRepository;

    private static final long MAX_FILE_SIZE = 1024 * 1024;

    private void validateRegisterDTO(RegisterCSVDTO dto) {
        if (dto.getName().trim().isEmpty()) {
            throw new FieldException("Name cannot be empty");
        }
        if (dto.getSurname().trim().isEmpty()) {
            throw new FieldException("Surname cannot be empty");
        }

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new FieldException("Invalid email format");
        }
        if (dto.getLogin().length() < 3) {
            throw new FieldException("Login must be at least 3 characters");
        }

        if (dto.getPassword().length() < 8) {
            throw new FieldException("Password must be at least 8 characters");
        }

        if (dto.getGroupTitle().trim().isEmpty()) {
            throw new FieldException("Group title cannot be empty");
        }
    }

    private String checkFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        String extension = fileName.substring(lastDotIndex + 1);
        return extension.toLowerCase();
    }

    private Boolean validationSizeFile(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }
        return true;
    }

    private Boolean checkEmail(String email) {
        Optional<Users> user = usersRepository.findUserByUserEmail(email);
        if (user.isPresent()) {
            return false;
        }
        return true;
    }


    private List<RegisterCSVDTO> parseToStudentDto(MultipartFile file) {
        if (!this.checkFile(file).equals("csv")) {
            throw new IncorrectFileFormatException("Only the CSV file is supported");
        }
        if (!validationSizeFile(file)) {
            throw new FileIsTooBigException("The file is too big");
        }

        try (Reader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            CsvToBean<RegisterCSVDTO> csvToBean = new CsvToBeanBuilder<RegisterCSVDTO>(reader)
                    .withType(RegisterCSVDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .withSeparator(',')
                    .withIgnoreEmptyLine(true)
                    .build();

            return csvToBean.parse();

        } catch (Exception e) {
            throw new FileException("File is incorrect. Error: " + e.getMessage());
        }
    }

    public List<RegisterCSVDTO> parse(MultipartFile file) {
        List<RegisterCSVDTO> registerCSVDTOList = parseToStudentDto(file);
        if (registerCSVDTOList.size() > 1) {
            throw new AmountOfDataError("Only 1 entry is accepted");
        }
        if (registerCSVDTOList.isEmpty()) {
            throw new AmountOfDataError("The file is empty");
        }

        for (RegisterCSVDTO registerDTO : registerCSVDTOList) {
            if (!checkEmail(registerDTO.getEmail())) {
                throw new MailIsBusyException("This mail is already in use");
            }
        }
        return registerCSVDTOList;
    }
}
