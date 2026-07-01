package org.example.repository;

import org.example.entity.Students;
import org.example.entity.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachersRepository extends JpaRepository<Teachers, Long> {
    Optional<Teachers> findByUser_Id(Long id);
}
