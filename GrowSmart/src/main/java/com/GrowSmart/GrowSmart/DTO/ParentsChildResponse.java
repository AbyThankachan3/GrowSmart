package com.GrowSmart.GrowSmart.DTO;

import com.GrowSmart.GrowSmart.Enums.Standard;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ParentsChildResponse {
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Standard standard;
    private UUID childId;
    private LocalDate dateOfBirth;
}
