package org.example.service;

import org.example.entity.Students;
import org.example.entity.Teachers;

public interface PermissionService {
    Boolean checkPermissionStudentViewing(Students source, Students target);
    Boolean checkPermissionTeacher(Teachers source, Students target);
}
