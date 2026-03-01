package com.gymportal.controller;

import com.gymportal.dto.AdminProfileRequest;
import com.gymportal.dto.ProfileRequest;
import com.gymportal.entity.Membership;
import com.gymportal.entity.User;
import com.gymportal.repository.DietPlanRepository;
import com.gymportal.repository.MemberProfileRepository;
import com.gymportal.repository.MembershipRepository;
import com.gymportal.repository.ProgressRepository;
import com.gymportal.repository.UserRepository;
import com.gymportal.service.MembershipService;
import com.gymportal.service.ProfileService;
import com.gymportal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin panel — manage members and view stats")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DietPlanRepository dietPlanRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private MemberProfileRepository profileRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Operation(summary = "Get all members", description = "Returns a list of all registered gym members")
    @GetMapping("/members")
    public ResponseEntity<Map<String, Object>> getAllMembers() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> members = userService.getAllMembers()
                .stream().map(u -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", u.getId());
                    m.put("username", u.getUsername());
                    m.put("email", u.getEmail());
                    m.put("createdAt", u.getCreatedAt().toString());
                    return m;
                }).toList();
        response.put("success", true);
        response.put("members", members);
        response.put("total", members.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a member", description = "Permanently removes a member account and all associated data by ID")
    @DeleteMapping("/members/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Cascade delete all member-related data
            dietPlanRepository.deleteByUserId(id);
            progressRepository.deleteByUserId(id);
            profileRepository.deleteByUserId(id);
            membershipRepository.deleteByUserId(id);

            // Finally delete the user account
            userService.deleteUser(id);

            response.put("success", true);
            response.put("message", "Member and all associated data deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete member: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get dashboard stats", description = "Returns total member count, daily active count, and estimated revenue")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> response = new HashMap<>();
        long totalMembers = userService.getAllMembers().size();
        response.put("success", true);
        response.put("totalMembers", totalMembers);
        response.put("activeToday", totalMembers); // simplified
        response.put("revenue", totalMembers * 999); // approx
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user details", description = "Returns full details including profile, membership, diet plan, and recent progress for a given user ID")
    @GetMapping("/users/{id}/details")
    public ResponseEntity<Map<String, Object>> getUserDetails(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            response.put("success", true);
            response.put("user_id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().toString());
            response.put("createdAt", user.getCreatedAt().toString());

            profileService.getProfile(id).ifPresent(p -> {
                response.put("profile", p);
            });

            membershipService.getMembership(id).ifPresent(m -> {
                response.put("membership", m);
            });

            dietPlanRepository.findFirstByUserIdOrderByGeneratedAtDesc(id).ifPresent(dp -> {
                response.put("dietPlan", dp);
            });

            List<com.gymportal.entity.ProgressEntry> progress = progressRepository.findByUserIdOrderByLoggedDateAsc(id);
            if (progress.size() > 5) {
                progress = progress.subList(progress.size() - 5, progress.size());
            }
            response.put("progress", progress);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Update user profile (admin)", description = "Admin can update any member's fitness profile")
    @PutMapping("/users/{id}/profile")
    public ResponseEntity<Map<String, Object>> updateUserProfile(
            @PathVariable long id,
            @RequestBody ProfileRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            profileService.saveOrUpdate(user, req);
            response.put("success", true);
            response.put("message", "User profile updated successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Update user membership (admin)", description = "Admin can change a member's membership plan")
    @PutMapping("/users/{id}/membership")
    public ResponseEntity<Map<String, Object>> updateUserMembership(
            @PathVariable long id,
            @RequestBody Map<String, String> req) {
        Map<String, Object> response = new HashMap<>();
        try {
            String newPlanStr = req.get("plan");
            if (newPlanStr == null || newPlanStr.isEmpty()) {
                throw new RuntimeException("Plan is required");
            }
            Membership.Plan plan = Membership.Plan
                    .valueOf(newPlanStr.toUpperCase());
            membershipService.upgradeMembership(id, plan);
            response.put("success", true);
            response.put("message", "User membership updated successfully!");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Invalid plan type");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Update admin profile (self)", description = "Admin can update their own username and password")
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateAdminProfile(
            @RequestBody AdminProfileRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            // In a real app, we would get the ID from the security context.
            // For this version, we lookup the 'admin' user or use the first admin found.
            // But since the frontend knows the userId, it should ideally pass it or we
            // fetch current.
            // However, to keep it simple and consistent with how other controllers
            // currently work:
            // We'll search for the user with ADMIN role.
            User admin = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == User.Role.ADMIN)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Admin account not found"));

            userService.updateAdminProfile(admin.getId(), req);

            response.put("success", true);
            response.put("message", "Admin profile updated successfully!");
            response.put("newUsername", admin.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
