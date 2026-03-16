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
import jakarta.validation.Valid;
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
        List<User> userList = userService.getAllMembers();
        
        // Fetch all memberships for these users in one go to avoid N+1 problem
        Map<Long, String> membershipPlans = membershipRepository.findAllByUserIn(userList)
                .stream()
                .filter(m -> m.getPlan() != null)
                .collect(java.util.stream.Collectors.toMap(
                        m -> m.getUser().getId(),
                        m -> m.getPlan().name()
                ));

        List<Map<String, Object>> members = userList.stream().map(u -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", u.getId());
                    m.put("username", u.getUsername());
                    m.put("email", u.getEmail());
                    m.put("createdAt", u.getCreatedAt().toString());
                    m.put("isLoggedIn", u.isLoggedIn());
                    m.put("plan", membershipPlans.getOrDefault(u.getId(), "NO PLAN"));
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
    }

    @Operation(summary = "Get dashboard stats", description = "Returns total member count, online count, and estimated revenue")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> response = new HashMap<>();

        // Fetch real data from DB
        long totalMembers = userRepository.countByRole(User.Role.MEMBER);
        // Synchronized to be the same as total members per user request
        long onlineNow = totalMembers;

        double revenue = membershipRepository.findAll().stream()
                .filter(m -> m.getAmountPaid() != null)
                .mapToDouble(Membership::getAmountPaid)
                .sum();

        response.put("success", true);
        response.put("totalMembers", totalMembers);
        response.put("onlineNow", onlineNow);
        response.put("revenue", revenue);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Reset database", description = "Clears all member data except admin accounts")
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetData() {
        Map<String, Object> response = new HashMap<>();
        userService.resetDatabase();
        response.put("success", true);
        response.put("message", "Database reset successfully! All member data cleared.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user details", description = "Returns full details including profile and membership for a given user ID")
    @GetMapping("/users/{id}/details")
    public ResponseEntity<Map<String, Object>> getUserDetails(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        response.put("success", true);
        response.put("user_id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("password", "ENCRYPTED");
        response.put("role", user.getRole().toString());
        response.put("createdAt", user.getCreatedAt().toString());

        profileService.getProfile(id).ifPresent(p -> {
            response.put("profile", p);
        });

        membershipService.getMembership(id).ifPresent(m -> {
            response.put("membership", m);
        });

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user profile (admin)", description = "Admin can update any member's fitness profile")
    @PutMapping("/users/{id}/profile")
    public ResponseEntity<Map<String, Object>> updateUserProfile(
            @PathVariable long id,
            @Valid @RequestBody ProfileRequest req) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        profileService.saveOrUpdate(user, req);
        response.put("success", true);
        response.put("message", "User profile updated successfully!");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user membership (admin)", description = "Admin can change a member's membership plan")
    @PutMapping("/users/{id}/membership")
    public ResponseEntity<Map<String, Object>> updateUserMembership(
            @PathVariable long id,
            @RequestBody Map<String, String> req) {
        Map<String, Object> response = new HashMap<>();
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
    }

    @Operation(summary = "Update admin profile (self)", description = "Admin can update their own username and password")
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateAdminProfile(
            @Valid @RequestBody AdminProfileRequest req) {
        Map<String, Object> response = new HashMap<>();
        // In a real app, we would get the ID from the security context.
        // For this version, we lookup the 'admin' user or use the first admin found.
        User admin = userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ADMIN)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin account not found"));

        userService.updateAdminProfile(admin.getId(), req);

        response.put("success", true);
        response.put("message", "Admin profile updated successfully!");
        response.put("newUsername", admin.getUsername());
        return ResponseEntity.ok(response);
    }
}
