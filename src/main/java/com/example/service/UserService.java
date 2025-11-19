package com.example.service;

import com.example.model.User;
import com.example.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User registerUser(String username, String rawPassword, String role) {
        if (role == null || role.isEmpty()) role = "USER";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole(role);
        return userRepository.save(user);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
