package org.example.repository;

import org.example.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {
    Optional<Students> findByUser_Id(Long id);

    @Transactional
    Stream<Students> findAllByGroup_Id(Long id);
}
