package org.example.repository;

import org.example.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByUser_Id(Long id);
    List<ConfirmationToken> findAllByIsShipped(Boolean isShipped);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM confirmation_token WHERE id = ?1", nativeQuery = true)
    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM confirmation_token WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(Long userId);

    @Query("SELECT t FROM ConfirmationToken t WHERE t.isShipped = false AND t.expiresAt > :now AND t.isError = false AND t.user.enabled = false")
    List<ConfirmationToken> findPendingTokens(@Param("now") LocalDateTime now);

    @Query("SELECT t FROM ConfirmationToken t WHERE t.isShipped = false AND t.expiresAt > :now AND t.isError = false AND t.user.enabled = false AND t.countRetry < :maxAttempts")
    List<ConfirmationToken> findPendingTokensWithRetryLimit(@Param("now") LocalDateTime now, @Param("maxAttempts") Integer maxAttempts);

    @Query("SELECT COUNT(t) FROM ConfirmationToken t WHERE t.isShipped = false AND t.expiresAt > :now AND t.isError = false AND t.user.enabled = false")
    Long countPendingTokens(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(t) FROM ConfirmationToken t WHERE t.isError = true")
    Long countErrorTokens();

    @Modifying
    @Transactional
    @Query("UPDATE ConfirmationToken t SET t.isError = true, t.errorMessage = :errorMessage WHERE t.id = :id")
    void markAsError(@Param("id") Long id, @Param("errorMessage") String errorMessage);

    @Modifying
    @Transactional
    @Query("UPDATE ConfirmationToken t SET t.countRetry = t.countRetry + 1, t.lastAttempt = :now, t.errorMessage = :errorMessage WHERE t.id = :id")
    void incrementCountRetry(@Param("id") Long id, @Param("now") LocalDateTime now, @Param("errorMessage") String errorMessage);
}