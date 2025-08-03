package com.GrowSmart.GrowSmart.DTO;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObservationDTO {
    private UUID id;
    private UUID childId;
    private String patternName;
    private Double confidence;
    private LocalDate analyzedAt;
}

