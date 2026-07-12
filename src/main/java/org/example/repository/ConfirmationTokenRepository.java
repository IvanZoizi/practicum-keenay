package org.example.repository;

import org.example.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByUser_Id(Long id);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM confirmation_token WHERE id = ?1", nativeQuery = true)
    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM confirmation_token WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(Long userId);
}
