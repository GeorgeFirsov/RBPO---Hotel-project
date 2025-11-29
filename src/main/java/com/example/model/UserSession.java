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
    private String userEmail;
    private String deviceId;
    @Column(length = 512)
    private String accessToken;
    @Column(length = 512)
    private String refreshToken;
    private Instant accessTokenExpiry;
    private Instant refreshTokenExpiry;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
}
