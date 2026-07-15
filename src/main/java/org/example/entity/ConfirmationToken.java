package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "confirmation_token")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private Users user;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_shipped")
    private Boolean isShipped;

    @Column(name = "is_error")
    private Boolean isError;

    @Column(name = "count_retry")
    private Integer countRetry;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttempt;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @PrePersist
    protected void onCreate() {
        isShipped = false;
        isError = false;
        countRetry = 0;
    }
}
