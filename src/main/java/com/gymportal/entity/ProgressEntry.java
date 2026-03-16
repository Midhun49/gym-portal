package com.gymportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "progress_entries")
@Data
@NoArgsConstructor
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
}
