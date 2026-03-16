package com.gymportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "memberships")
@Data
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private Plan plan;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double amountPaid;

    public enum Plan {
        BASIC, STANDARD, PREMIUM;

        public Double getPrice() {
            return switch (this) {
                case BASIC -> 999.0;
                case STANDARD -> 1999.0;
                case PREMIUM -> 3499.0;
            };
        }

        public String getDescription() {
            return switch (this) {
                case BASIC -> "Access to gym floor, basic equipment";
                case STANDARD -> "Basic + Group classes, locker room";
                case PREMIUM -> "Standard + Personal trainer, dietitian, spa";
            };
        }
    }

    public enum Status {
        ACTIVE, EXPIRED, SUSPENDED
    }
}
