package com.GrowSmart.GrowSmart.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "growth_records")
public class GrowthRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Child child;

    private LocalDate recordedAt;

    private Double heightCm;

    private Double weightKg;

    private String source; // e.g. "parent_upload", "clinic_checkup"

    private String note;
}
