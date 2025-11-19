package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WhoAmIController {
    @GetMapping("/whoami")
    public Map<String, Object> whoAmI(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        if (authentication == null) {
            result.put("authenticated", false);
            return result;
        }
        result.put("authenticated", true);
        result.put("username", authentication.getName());
        String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).map(a -> a.startsWith("ROLE_") ? a.substring(5) : a).findFirst().orElse("UNKNOWN");
        result.put("role", role);
        return result;
    }
}
