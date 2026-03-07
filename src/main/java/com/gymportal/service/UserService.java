package com.gymportal.service;

import com.gymportal.dto.RegisterRequest;
import com.gymportal.entity.User;
import com.gymportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MembershipService membershipService;

    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User(
                req.getUsername(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                User.Role.MEMBER);
        user.setPlainPassword(req.getPassword());
        User saved = userRepository.save(user);
        // Auto-create BASIC membership
        membershipService.createDefaultMembership(saved);
        return saved;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean authenticate(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .map(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .orElse(false);
    }

    public List<User> getAllMembers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.MEMBER)
                .toList();
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public void updateAdminProfile(long id, com.gymportal.dto.AdminProfileRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin profiles can be updated here");
        }

        // Validate current password
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password does not match");
        }

        // Update username if provided
        if (req.getNewUsername() != null && !req.getNewUsername().trim().isEmpty()) {
            if (!req.getNewUsername().equals(user.getUsername())
                    && userRepository.existsByUsername(req.getNewUsername())) {
                throw new RuntimeException("Username already taken");
            }
            user.setUsername(req.getNewUsername());
        }

        // Update password if provided
        if (req.getNewPassword() != null && !req.getNewPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
            user.setPlainPassword(req.getNewPassword());
        }

        userRepository.save(user);
    }
}
