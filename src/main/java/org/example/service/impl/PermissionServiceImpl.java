package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Groups;
import org.example.entity.Students;
import org.example.entity.Teachers;
import org.example.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    @Override
    public Boolean checkPermissionStudentViewing(Students source, Students target) {
        return Objects.equals(source.getGroup().getId(), target.getGroup().getId());
    }

    @Override
    public Boolean checkPermissionTeacher(Teachers source, Students target) {
        for (Groups groups : source.getGroups()) {
            if (groups.getId().equals(target.getGroup().getId())) {
                return true;
            }
        }
        return false;
    }
}
