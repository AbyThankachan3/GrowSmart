package com.GrowSmart.GrowSmart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatternDTO {
    private double confidence;
    private String pattern;
}
