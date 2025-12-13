package com.example.controller;

import com.example.model.User;
import com.example.service.TokenService;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    // Регистрация пользователя (как было)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        String role = request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN") ? "ADMIN" : "USER";
        User user = userService.registerUser(request.getUsername(), request.getPassword(), role);
        return ResponseEntity.ok("User registered: " + user.getUsername());
    }

    // Логин: возвращаем пару токенов
    @PostMapping("/login")
    public ResponseEntity<TokenService.TokenResponse> login(@RequestBody LoginRequest request) {
        String deviceId = request.getDeviceId() != null ? request.getDeviceId() : "unknown";
        TokenService.TokenResponse tokens = tokenService.login(request.getUsername(), request.getPassword(), deviceId);
        return ResponseEntity.ok(tokens);
    }

    // Обновление по refresh-токену
    @PostMapping("/refresh")
    public ResponseEntity<TokenService.TokenResponse> refresh(@RequestBody RefreshRequest request) {
        TokenService.TokenResponse tokens = tokenService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }

    // DTO для регистрации
    public static class RegisterRequest {
        private String username;
        private String password;
        private String role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    // DTO для логина
    public static class LoginRequest {
        private String username;
        private String password;
        private String deviceId;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

    // DTO для refresh
    public static class RefreshRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
