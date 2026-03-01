package com.gymportal.controller;

import com.gymportal.entity.User;
import com.gymportal.repository.UserRepository;
import com.gymportal.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@Tag(name = "Progress Tracker", description = "Log and retrieve daily weight, calorie, and water intake entries")
public class ProgressController {

    @Autowired
    private ProgressService progressService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Get progress entries", description = "Fetches all logged progress entries for a member, ordered by date")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getProgress(@PathVariable long userId) {
        List<Map<String, Object>> result = progressService.getProgress(userId)
                .stream().map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", e.getId());
                    m.put("weightKg", e.getWeightKg());
                    m.put("caloriesConsumed", e.getCaloriesConsumed());
                    m.put("waterIntakeMl", e.getWaterIntakeMl());
                    m.put("loggedDate", e.getLoggedDate().toString());
                    m.put("notes", e.getNotes());
                    return m;
                }).toList();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Log progress entry", description = "Records daily weight, calories consumed, water intake, and optional notes for a member")
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> logProgress(
            @PathVariable long userId,
            @RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Double weight = body.get("weightKg") != null ? Double.parseDouble(body.get("weightKg").toString()) : null;
            Integer calories = body.get("caloriesConsumed") != null
                    ? Integer.parseInt(body.get("caloriesConsumed").toString())
                    : null;
            Integer water = body.get("waterIntakeMl") != null ? Integer.parseInt(body.get("waterIntakeMl").toString())
                    : null;
            String notes = (String) body.get("notes");

            progressService.logProgress(user, weight, calories, water, notes);
            response.put("success", true);
            response.put("message", "Progress logged successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
