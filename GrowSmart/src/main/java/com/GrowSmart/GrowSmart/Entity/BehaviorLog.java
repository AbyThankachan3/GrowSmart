package com.GrowSmart.GrowSmart.Entity;

import com.GrowSmart.GrowSmart.Enums.SourceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "behavior_logs")
public class BehaviorLog {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Child child;

    @ManyToOne
    private User loggedBy;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private SourceType source; // PARENT, FACULTY, AI

    private String eventContext; // "Lunch", "Group Activity", etc.

    @Column(columnDefinition = "TEXT")
    private String rawText;

    @ElementCollection
    @CollectionTable(name = "behavior_log_emotions", joinColumns = @JoinColumn(name = "behavior_log_id"))
    @Column(name = "emotion")
    private List<String> emotionDetected;


}