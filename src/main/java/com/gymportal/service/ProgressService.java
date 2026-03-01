package com.gymportal.service;

import com.gymportal.entity.ProgressEntry;
import com.gymportal.entity.User;
import com.gymportal.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    public ProgressEntry logProgress(User user, Double weight, Integer calories, Integer water, String notes) {
        ProgressEntry entry = new ProgressEntry();
        entry.setUser(user);
        entry.setWeightKg(weight);
        entry.setCaloriesConsumed(calories);
        entry.setWaterIntakeMl(water);
        entry.setLoggedDate(LocalDate.now());
        entry.setNotes(notes);
        return progressRepository.save(entry);
    }

    public List<ProgressEntry> getProgress(long userId) {
        return progressRepository.findByUserIdOrderByLoggedDateAsc(userId);
    }
}
