package com.gymportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_profiles")
@Data
@NoArgsConstructor
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String fullName;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Double heightCm;
    private Double weightKg;

    @Enumerated(EnumType.STRING)
    private FitnessGoal fitnessGoal;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    private DietType dietType = DietType.VEGETARIAN;

    private String phoneNumber;
    private String address;

    public enum Gender { MALE, FEMALE }
    public enum FitnessGoal { LOSE_WEIGHT, GAIN_MUSCLE, MAINTAIN, IMPROVE_ENDURANCE }
    public enum ActivityLevel { SEDENTARY, LIGHT, MODERATE, ACTIVE, VERY_ACTIVE }
    public enum DietType { VEGETARIAN, NON_VEGETARIAN }
}
