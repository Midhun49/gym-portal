package com.gymportal.controller;

import com.gymportal.dto.LoginRequest;
import com.gymportal.dto.RegisterRequest;
import com.gymportal.entity.User;
import com.gymportal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Member registration, login, and logout")
public class AuthController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Register a new member", description = "Creates a new user account and saves it in the database")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest req) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.register(req);
        response.put("success", true);
        response.put("message", "Registration successful!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login member", description = "Authenticates a user and starts a session")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        Map<String, Object> response = new HashMap<>();
        boolean authenticated = userService.authenticate(req.getUsername(), req.getPassword());
        
        if (!authenticated) {
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }

        User user = userService.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        userService.setLoggedInStatus(user.getId(), true);

        response.put("success", true);
        response.put("message", "Login successful!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout", description = "Clears user session status")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, Long> req) {
        Map<String, Object> response = new HashMap<>();
        Long userId = req.get("userId");
        if (userId != null) {
            userService.setLoggedInStatus(userId, false);
            response.put("success", true);
            response.put("message", "Logged out successfully");
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("message", "UserId required");
        return ResponseEntity.badRequest().body(response);
    }
}
