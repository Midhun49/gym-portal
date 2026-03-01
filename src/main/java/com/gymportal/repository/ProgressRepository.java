package com.gymportal.repository;

import com.gymportal.entity.ProgressEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ProgressRepository extends JpaRepository<ProgressEntry, Long> {
    List<ProgressEntry> findByUserIdOrderByLoggedDateAsc(Long userId);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
