package org.example.repository;

import org.example.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query(value = "SELECT * FROM users WHERE enabled = true AND login = ?1", nativeQuery = true)
    Optional<Users> findByLogin(String login);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.enabled = ?1 WHERE u.id = ?2")
    void updateEnable(Boolean status, Long id);

    @Query("SELECT COALESCE(s.email, t.email) " +
            "FROM Users u " +
            "LEFT JOIN Students s ON s.user.id = u.id " +
            "LEFT JOIN Teachers t ON t.user.id = u.id " +
            "WHERE u.id = ?1")
    Optional<String> findEmailByUserId(Long userId);
}