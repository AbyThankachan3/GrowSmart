package com.GrowSmart.GrowSmart.Entity;

import com.GrowSmart.GrowSmart.Enums.Standard;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue
    private UUID id;

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    @ManyToOne
    private User guardian;

    @Enumerated(EnumType.STRING)
    private Standard standard;

    private String studentCode; // School-assigned code

    // Relations
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<Consent> consents;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<BehaviorLog> behaviorLogs;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<GrowthRecord> growthRecords;

    // Getters, Setters
}
