package org.example.repository;

import org.example.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {

    @Query(value = "SELECT s.* FROM students s " +
            "JOIN users u ON s.users_id = u.id " +
            "WHERE s.users_id = ?1 AND u.enabled = true",
            nativeQuery = true)
    Optional<Students> findByUser_Id(Long id);

    @Query(value = "SELECT s.* FROM students s " +
            "JOIN users u ON s.users_id = u.id " +
            "WHERE s.group_id = ?1 AND u.enabled = true",
            nativeQuery = true)
    Stream<Students> findAllByGroup_Id(Long id);

    @Override
    @Query(value = "SELECT s.* FROM students s " +
            "JOIN users u ON s.users_id = u.id " +
            "WHERE u.enabled = true",
            nativeQuery = true)
    List<Students> findAll();

    @Override
    @Query(value = "SELECT s.* FROM students s " +
            "JOIN users u ON s.users_id = u.id " +
            "WHERE s.id = ?1 AND u.enabled = true",
            nativeQuery = true)
    Optional<Students> findById(Long id);
}