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
public class ConsentDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID id;
    private UUID childId;
    private UUID userId;
    private boolean canView;
    private boolean canLog;
    private LocalDateTime grantedAt;
}

