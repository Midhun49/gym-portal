package com.gymportal.controller;

import com.gymportal.dto.ProfileRequest;
import com.gymportal.entity.User;
import com.gymportal.repository.UserRepository;
import com.gymportal.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile", description = "Member profile — get and save personal fitness details")
public class ProfileController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Get profile", description = "Returns the fitness profile of a member including height, weight, goal, and diet type")
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable long userId) {
        Map<String, Object> response = new HashMap<>();
        return profileService.getProfile(userId).map(p -> {
            response.put("success", true);
            response.put("fullName", p.getFullName());
            response.put("age", p.getAge());
            response.put("gender", p.getGender());
            response.put("heightCm", p.getHeightCm());
            response.put("weightKg", p.getWeightKg());
            response.put("fitnessGoal", p.getFitnessGoal());
            response.put("activityLevel", p.getActivityLevel());
            response.put("dietType", p.getDietType());
            response.put("phoneNumber", p.getPhoneNumber());
            response.put("address", p.getAddress());
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Profile not found");
            return ResponseEntity.ok(response);
        });
    }

    @Operation(summary = "Save or update profile", description = "Creates or updates the member's fitness profile details")
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> saveProfile(
            @PathVariable long userId,
            @RequestBody ProfileRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            profileService.saveOrUpdate(user, req);
            response.put("success", true);
            response.put("message", "Profile saved successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
