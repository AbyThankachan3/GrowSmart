package com.GrowSmart.GrowSmart.DTO;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID id;
    private UUID childId;
    private String title;
    private String description;
    private String basedOnFlag; // Optional: what behavior or flag triggered it
    private boolean resolved;
    private LocalDateTime suggestedAt;
}

