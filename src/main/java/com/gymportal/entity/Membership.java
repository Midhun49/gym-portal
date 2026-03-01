package com.gymportal.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private Plan plan = Plan.BASIC;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private Double amountPaid;

    public enum Plan {
        BASIC, STANDARD, PREMIUM;

        public Double getPrice() {
            return switch(this) {
                case BASIC -> 999.0;
                case STANDARD -> 1999.0;
                case PREMIUM -> 3499.0;
            };
        }

        public String getDescription() {
            return switch(this) {
                case BASIC -> "Access to gym floor, basic equipment";
                case STANDARD -> "Basic + Group classes, locker room";
                case PREMIUM -> "Standard + Personal trainer, dietitian, spa";
            };
        }
    }

    public enum Status { ACTIVE, EXPIRED, SUSPENDED }

    // Constructors
    public Membership() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }
}
