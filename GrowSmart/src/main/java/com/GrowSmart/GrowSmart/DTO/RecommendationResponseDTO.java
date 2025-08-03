package com.GrowSmart.GrowSmart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationResponseDTO {
    private String basedOnFlag;
    private String title;
    private String description;
}