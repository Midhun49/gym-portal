package com.gymportal.repository;

import com.gymportal.entity.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    List<DietPlan> findByUserIdOrderByGeneratedAtDesc(Long userId);

    Optional<DietPlan> findFirstByUserIdOrderByGeneratedAtDesc(Long userId);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
