package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userEmail;                // логин/емейл пользователя
    private String deviceId;                 // айди устройства (можно передавать с клиента)

    @Column(length = 512)
    private String accessToken;              // Access-токен (для истории, можно не использовать)

    @Column(length = 512)
    private String refreshToken;             // Refresh-токен, по нему ищем сессию

    private Instant accessTokenExpiry;       // когда истечёт access
    private Instant refreshTokenExpiry;      // когда истечёт refresh

    @Enumerated(EnumType.STRING)
    private SessionStatus status;            // ACTIVE / USED / REVOKED
}
