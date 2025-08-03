package com.GrowSmart.GrowSmart.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "observations")

public class Observation {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    private String patternName; // e.g. "Social Withdrawal"

    private Double confidence;

    private LocalDate analyzedAt;
}
