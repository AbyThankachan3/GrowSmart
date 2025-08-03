package com.GrowSmart.GrowSmart.Entity;

import com.GrowSmart.GrowSmart.Enums.Standard;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "faculty_standard_assignments")
public class FacultyStandardAssignment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User faculty;

    @Enumerated(EnumType.STRING)
    private Standard standard;
}
