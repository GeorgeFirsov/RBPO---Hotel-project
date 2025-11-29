package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secret = "very_super_secret_key_1234567890_qwerty";

    private final long accessTokenValidityMs = 15 * 60 * 1000;
    private final long refreshTokenValidityMs = 7L * 24 * 60 * 60 * 1000;

    public JwtTokenProvider() {
    }

    public String createAccessToken(String username, String role) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(accessTokenValidityMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("tokenType", "ACCESS")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String createRefreshToken(String username, String deviceId) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(refreshTokenValidityMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("deviceId", deviceId)
                .claim("tokenType", "REFRESH")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = parseClaims(token);
            String type = claims.get("tokenType", String.class);
            return "ACCESS".equals(type);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            String type = claims.get("tokenType", String.class);
            return "REFRESH".equals(type);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    public long getAccessTokenValidityMs() {
        return accessTokenValidityMs;
    }

    public long getRefreshTokenValidityMs() {
        return refreshTokenValidityMs;
    }
}
