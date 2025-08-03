package com.GrowSmart.GrowSmart.DTO;

import com.GrowSmart.GrowSmart.Enums.Role;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID id;
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role; // PARENT, FACULTY, COUNSELOR, CLINIC, ADMIN
    private String organization;
    private String password;
}
