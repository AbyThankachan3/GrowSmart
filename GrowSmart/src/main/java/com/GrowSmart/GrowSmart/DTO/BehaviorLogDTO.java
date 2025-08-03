package com.GrowSmart.GrowSmart.DTO;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorLogDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID id;
    private String childId;
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID loggedByUserId;
    @JsonSetter(nulls = Nulls.SKIP)
    private String source; // PARENT, FACULTY, AI
    private String eventContext; // e.g. “Lunch”, “Morning”
    private String rawText;
    @JsonSetter(nulls = Nulls.SKIP)
    private List<String> emotionDetected;
}

