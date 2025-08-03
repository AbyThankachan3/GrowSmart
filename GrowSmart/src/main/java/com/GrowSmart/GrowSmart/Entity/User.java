package com.GrowSmart.GrowSmart.Entity;

import com.GrowSmart.GrowSmart.Enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String fullName;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; // PARENT, FACULTY, COUNSELOR, CLINIC, ADMIN

    private LocalDateTime createdAt = LocalDateTime.now();

    private String password;
}
