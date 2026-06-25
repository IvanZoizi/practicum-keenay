package org.example.repository;

import org.example.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {
    Optional<Students> findById(Long id);
    @Query("SELECT s FROM Students s")
    Stream<Students> findAllAsStream();
    void deleteById(Long id);
}
