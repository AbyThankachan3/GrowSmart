package com.GrowSmart.GrowSmart.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Child child;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String basedOnFlag; // e.g. "Sensory Overload"

    private boolean resolved = false;

    private LocalDateTime suggestedAt = LocalDateTime.now();
}
