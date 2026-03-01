package com.gymportal.dto;

import com.gymportal.entity.MemberProfile;

public class ProfileRequest {
    private String fullName;
    private Integer age;
    private MemberProfile.Gender gender;
    private Double heightCm;
    private Double weightKg;
    private MemberProfile.FitnessGoal fitnessGoal;
    private MemberProfile.ActivityLevel activityLevel;
    private MemberProfile.DietType dietType;
    private String phoneNumber;
    private String address;

    public ProfileRequest() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public MemberProfile.Gender getGender() { return gender; }
    public void setGender(MemberProfile.Gender gender) { this.gender = gender; }
    public Double getHeightCm() { return heightCm; }
    public void setHeightCm(Double heightCm) { this.heightCm = heightCm; }
    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }
    public MemberProfile.FitnessGoal getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(MemberProfile.FitnessGoal fitnessGoal) { this.fitnessGoal = fitnessGoal; }
    public MemberProfile.ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(MemberProfile.ActivityLevel activityLevel) { this.activityLevel = activityLevel; }
    public MemberProfile.DietType getDietType() { return dietType; }
    public void setDietType(MemberProfile.DietType dietType) { this.dietType = dietType; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
