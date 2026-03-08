package com.gymportal.repository;

import com.gymportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRole(User.Role role);

    long countByIsLoggedInTrue();

    long countByRoleAndIsLoggedInTrue(User.Role role);

    long countByRole(User.Role role);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE User u SET u.isLoggedIn = false")
    void resetAllLoginStatus();

    void deleteByRole(User.Role role);
}
