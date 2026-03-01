package com.gymportal.controller;

import com.gymportal.entity.DietPlan;
import com.gymportal.entity.MemberProfile;
import com.gymportal.entity.User;
import com.gymportal.repository.UserRepository;
import com.gymportal.service.DietAIService;
import com.gymportal.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diet")
@Tag(name = "Diet Plan", description = "AI-generated Indian diet plan generation and history")
public class DietPlanController {

    @Autowired
    private DietAIService dietAIService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Generate AI diet plan", description = "Uses member profile data to generate a personalised Indian diet plan")
    @PostMapping("/generate/{userId}")
    public ResponseEntity<Map<String, Object>> generateDietPlan(@PathVariable long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            MemberProfile profile = profileService.getProfile(userId)
                    .orElseThrow(() -> new RuntimeException("Please complete your profile first!"));

            DietPlan plan = dietAIService.generateDietPlan(profile, user);
            return ResponseEntity.ok(buildPlanResponse(plan, true, null));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get latest diet plan", description = "Returns the most recently generated diet plan for a member")
    @GetMapping("/latest/{userId}")
    public ResponseEntity<Map<String, Object>> getLatestPlan(@PathVariable long userId) {
        return dietAIService.getLatestDietPlan(userId)
                .map(plan -> ResponseEntity.ok(buildPlanResponse(plan, true, null)))
                .orElseGet(() -> {
                    Map<String, Object> r = new HashMap<>();
                    r.put("success", false);
                    r.put("message", "No diet plan found. Generate one first.");
                    return ResponseEntity.ok(r);
                });
    }

    @Operation(summary = "Get diet plan history", description = "Returns all previously generated diet plans for a member")
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getPlanHistory(@PathVariable long userId) {
        List<Map<String, Object>> result = dietAIService.getAllDietPlans(userId)
                .stream()
                .map(p -> buildPlanResponse(p, true, null))
                .toList();
        return ResponseEntity.ok(result);
    }

    private Map<String, Object> buildPlanResponse(DietPlan plan, boolean success, String msg) {
        Map<String, Object> r = new HashMap<>();
        r.put("success", success);
        if (msg != null)
            r.put("message", msg);
        r.put("id", plan.getId());
        r.put("caloriesTarget", plan.getCaloriesTarget());
        r.put("proteinG", plan.getProteinG());
        r.put("carbsG", plan.getCarbsG());
        r.put("fatsG", plan.getFatsG());
        r.put("bmi", plan.getBmi());
        r.put("bmr", plan.getBmr());
        r.put("tdee", plan.getTdee());
        r.put("breakfast", plan.getBreakfast());
        r.put("morningSnack", plan.getMorningSnack());
        r.put("lunch", plan.getLunch());
        r.put("eveningSnack", plan.getEveningSnack());
        r.put("dinner", plan.getDinner());
        r.put("postWorkout", plan.getPostWorkout());
        r.put("dietNotes", plan.getDietNotes());
        r.put("generatedAt", plan.getGeneratedAt().toString());
        return r;
    }
}
