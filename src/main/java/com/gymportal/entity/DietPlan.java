package com.gymportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diet_plans")
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer caloriesTarget;
    private Integer proteinG;
    private Integer carbsG;
    private Integer fatsG;

    private Double bmi;
    private Double bmr;
    private Double tdee;

    @Column(columnDefinition = "TEXT")
    private String breakfast;

    @Column(columnDefinition = "TEXT")
    private String morningSnack;

    @Column(columnDefinition = "TEXT")
    private String lunch;

    @Column(columnDefinition = "TEXT")
    private String eveningSnack;

    @Column(columnDefinition = "TEXT")
    private String dinner;

    @Column(columnDefinition = "TEXT")
    private String postWorkout;

    @Column(columnDefinition = "TEXT")
    private String dietNotes;

    private LocalDateTime generatedAt = LocalDateTime.now();

    // Constructors
    public DietPlan() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getCaloriesTarget() { return caloriesTarget; }
    public void setCaloriesTarget(Integer caloriesTarget) { this.caloriesTarget = caloriesTarget; }
    public Integer getProteinG() { return proteinG; }
    public void setProteinG(Integer proteinG) { this.proteinG = proteinG; }
    public Integer getCarbsG() { return carbsG; }
    public void setCarbsG(Integer carbsG) { this.carbsG = carbsG; }
    public Integer getFatsG() { return fatsG; }
    public void setFatsG(Integer fatsG) { this.fatsG = fatsG; }
    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }
    public Double getBmr() { return bmr; }
    public void setBmr(Double bmr) { this.bmr = bmr; }
    public Double getTdee() { return tdee; }
    public void setTdee(Double tdee) { this.tdee = tdee; }
    public String getBreakfast() { return breakfast; }
    public void setBreakfast(String breakfast) { this.breakfast = breakfast; }
    public String getMorningSnack() { return morningSnack; }
    public void setMorningSnack(String morningSnack) { this.morningSnack = morningSnack; }
    public String getLunch() { return lunch; }
    public void setLunch(String lunch) { this.lunch = lunch; }
    public String getEveningSnack() { return eveningSnack; }
    public void setEveningSnack(String eveningSnack) { this.eveningSnack = eveningSnack; }
    public String getDinner() { return dinner; }
    public void setDinner(String dinner) { this.dinner = dinner; }
    public String getPostWorkout() { return postWorkout; }
    public void setPostWorkout(String postWorkout) { this.postWorkout = postWorkout; }
    public String getDietNotes() { return dietNotes; }
    public void setDietNotes(String dietNotes) { this.dietNotes = dietNotes; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
