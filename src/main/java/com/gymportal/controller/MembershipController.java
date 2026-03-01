package com.gymportal.controller;

import com.gymportal.entity.Membership;
import com.gymportal.service.MembershipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/membership")
@Tag(name = "Membership", description = "View, upgrade, and list gym membership plans")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @Operation(summary = "Get membership", description = "Returns the current membership plan and status for a member")
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getMembership(@PathVariable long userId) {
        Map<String, Object> response = new HashMap<>();
        return membershipService.getMembership(userId).map(m -> {
            response.put("success", true);
            response.put("plan", m.getPlan());
            response.put("status", m.getStatus());
            response.put("startDate", m.getStartDate().toString());
            response.put("endDate", m.getEndDate().toString());
            response.put("amountPaid", m.getAmountPaid());
            response.put("description", m.getPlan().getDescription());
            response.put("price", m.getPlan().getPrice());
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "No membership found");
            return ResponseEntity.ok(response);
        });
    }

    @Operation(summary = "Upgrade membership", description = "Upgrades a member's plan. Valid plans: BASIC, STANDARD, PREMIUM")
    @PostMapping("/{userId}/upgrade")
    public ResponseEntity<Map<String, Object>> upgradeMembership(
            @PathVariable long userId,
            @RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        try {
            Membership.Plan plan = Membership.Plan.valueOf(body.get("plan"));
            Membership m = membershipService.upgradeMembership(userId, plan);
            response.put("success", true);
            response.put("message", "Membership upgraded to " + plan + "!");
            response.put("plan", m.getPlan());
            response.put("endDate", m.getEndDate().toString());
            response.put("amountPaid", m.getAmountPaid());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "List all plans", description = "Returns all available membership tiers with price and description")
    @GetMapping("/plans")
    public ResponseEntity<Map<String, Object>> getAllPlans() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> plans = new HashMap<>();
        for (Membership.Plan plan : Membership.Plan.values()) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("price", plan.getPrice());
            detail.put("description", plan.getDescription());
            plans.put(plan.name(), detail);
        }
        response.put("success", true);
        response.put("plans", plans);
        return ResponseEntity.ok(response);
    }
}
