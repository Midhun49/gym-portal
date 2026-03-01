package com.gymportal.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "progress_entries")
public class ProgressEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double weightKg;
    private Integer caloriesConsumed;
    private Integer waterIntakeMl;
    private LocalDate loggedDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Constructors
    public ProgressEntry() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }
    public Integer getCaloriesConsumed() { return caloriesConsumed; }
    public void setCaloriesConsumed(Integer caloriesConsumed) { this.caloriesConsumed = caloriesConsumed; }
    public Integer getWaterIntakeMl() { return waterIntakeMl; }
    public void setWaterIntakeMl(Integer waterIntakeMl) { this.waterIntakeMl = waterIntakeMl; }
    public LocalDate getLoggedDate() { return loggedDate; }
    public void setLoggedDate(LocalDate loggedDate) { this.loggedDate = loggedDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
