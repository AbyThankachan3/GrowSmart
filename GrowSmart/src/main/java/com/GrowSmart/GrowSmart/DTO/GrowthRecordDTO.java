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
public class GrowthRecordDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID id;
    private UUID childId;
    private LocalDate recordedAt;
    private Double heightCm;
    private Double weightKg;
    private String source; // e.g. “parent_upload”
    private String note;
}
