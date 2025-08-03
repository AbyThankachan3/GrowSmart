package com.GrowSmart.GrowSmart.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "consents")
public class Consent {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Child child;

    @ManyToOne
    private User user;

    private boolean canView;
    private boolean canLog;

    private LocalDateTime grantedAt = LocalDateTime.now();
}