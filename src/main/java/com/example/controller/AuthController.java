package com.example.controller;

import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    public static class RegisterRequest {
        public String username;
        public String password;
        public String role;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        if (userService.existsByUsername(request.username)) {
            response.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }
        String role = request.role != null && request.role.equalsIgnoreCase("ADMIN") ? "ADMIN" : "USER";
        userService.registerUser(request.username, request.password, role);
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }
}
