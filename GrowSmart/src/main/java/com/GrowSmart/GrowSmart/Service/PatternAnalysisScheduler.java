package com.GrowSmart.GrowSmart.Service;

import com.GrowSmart.GrowSmart.Entity.BehaviorLog;
import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.Observation;
import com.GrowSmart.GrowSmart.Repository.BehaviorLogRepository;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.ObservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PatternAnalysisScheduler {

    private final BehaviorLogRepository behaviorLogRepository;
    private final ObservationRepository observationRepository;
    private final AzureOpenAIService azureOpenAIService;
    private final ChildRepository childRepository;

    public PatternAnalysisScheduler(BehaviorLogRepository behaviorLogRepository,
                                    ObservationRepository observationRepository,
                                    AzureOpenAIService azureOpenAIService,
                                    ChildRepository childRepository) {
        this.behaviorLogRepository = behaviorLogRepository;
        this.observationRepository = observationRepository;
        this.azureOpenAIService = azureOpenAIService;
        this.childRepository = childRepository;
    }

    @Scheduled(cron = "0 0 1 * * *") // Runs daily at 1 AM
    @Transactional
//    @Scheduled(cron = "*/10 * * * * *")
    public void analyzeBehaviorPatterns() {
        List<Child> allChildren = childRepository.findAll();

        for (Child child : allChildren) {
            List<BehaviorLog> logs = behaviorLogRepository.findByChildId(child.getId());

            if (logs.isEmpty()) continue;

            StringBuilder combinedLogs = new StringBuilder();
            for (BehaviorLog log : logs) {
                combinedLogs.append(log.getRawText()).append("\n");
            }

            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", "You are an AI that analyzes child behavior logs and identifies behavioral patterns such as social withdrawal, aggression, anxiety, etc. Respond with a concise pattern name and confidence. returns a JSON like: {\"pattern\": \"Social Withdrawal\", \"confidence\": 0.87}. Only output this JSON. No explanation"),
                    Map.of("role", "user", "content", combinedLogs.toString())
            );

            try {
                String gptResponse = azureOpenAIService.chat(messages, 100, false);

                System.out.println("GPT Response: " + gptResponse);
                // Assume the response is in format: "pattern: Social Withdrawal, confidence: 0.85"
                String pattern = extractPatternName(gptResponse);
                double confidence = extractConfidence(gptResponse);

                Observation observation = new Observation();
                observation.setChild(child);
                observation.setPatternName(pattern);
                observation.setConfidence(confidence);
                observation.setAnalyzedAt(LocalDate.now());

                observationRepository.save(observation);

            } catch (Exception e) {
                System.err.println("Failed to analyze pattern for child: " + child.getId());
                e.printStackTrace();
            }
        }
    }

    private String extractPatternName(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            String innerJsonString = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode innerJson = mapper.readTree(innerJsonString);

            return innerJson.path("pattern").asText("Unknown");
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private double extractConfidence(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            String innerJsonString = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode innerJson = mapper.readTree(innerJsonString);

            return innerJson.path("confidence").asDouble(0.0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

}
