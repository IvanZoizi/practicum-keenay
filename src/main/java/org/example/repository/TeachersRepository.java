package org.example.repository;

import org.example.entity.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeachersRepository extends JpaRepository<Teachers, Long> {

    @Query("SELECT t FROM Teachers t WHERE t.user.id = ?1 AND t.user.enabled = true")
    Optional<Teachers> findByUser_Id(Long userId);


    @Override
    @Query("SELECT t FROM Teachers t WHERE t.user.enabled = true")
    List<Teachers> findAll();

    @Override
    @Query("SELECT t FROM Teachers t WHERE t.id = ?1 AND t.user.enabled = true")
    Optional<Teachers> findById(Long id);

    @Query("SELECT t FROM Teachers t WHERE t.email = ?1 AND t.user.enabled = true")
    Optional<Teachers> findByEmail(String email);
}