package com.GrowSmart.GrowSmart.Service;

import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.Observation;
import com.GrowSmart.GrowSmart.Entity.Recommendation;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.ObservationRepository;
import com.GrowSmart.GrowSmart.Repository.RecommendationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationGenerator {

    private final ObservationRepository observationRepository;
    private final ChildRepository childRepository;
    private final AzureOpenAIService azureOpenAIService;
    private final RecommendationRepository recommendationRepository;

    public RecommendationGenerator(ObservationRepository observationRepository, ChildRepository childRepository, AzureOpenAIService azureOpenAIService, RecommendationRepository recommendationRepository) {
        this.observationRepository = observationRepository;
        this.childRepository = childRepository;
        this.azureOpenAIService = azureOpenAIService;
        this.recommendationRepository = recommendationRepository;
    }


//    @Scheduled(cron = "*/10 * * * * *") // every 10 seconds for testing
    @Scheduled(cron = "0 0 1 * * *") // Runs daily at 1 AM
    public void generateRecommendations() {
        List<Child> children = childRepository.findAll();

        for (Child child : children) {
            List<Observation> observations = observationRepository.findByChildId(child.getId());

            if (observations.isEmpty()) continue;

            StringBuilder patternText = new StringBuilder();
            for (Observation obs : observations) {
                patternText.append("Pattern: ").append(obs.getPatternName())
                        .append(", Confidence: ").append(obs.getConfidence()).append("\n");
            }

            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", "You are a child development expert. Given behavioral patterns, recommend helpful strategies and support plans for caregivers. Respond in JSON with fields: title, description -> should be solution like what measures should be followed to better the situation (like what coping mechanism must be followed like any type of particular games, or stories or any situational gameplay but a practical approach), basedOn (an array of { behavior, confidence, strategies })."),
                    Map.of("role", "user", "content", patternText.toString())
            );

            try {
                String gptResponse = azureOpenAIService.chat(messages, 600, false);
                System.out.println("GPT Raw Response: " + gptResponse);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(gptResponse);

                JsonNode choicesNode = root.get("choices");
                if (choicesNode == null || !choicesNode.isArray() || choicesNode.size() == 0) {
                    System.err.println("No choices found in GPT response.");
                    continue;
                }

                JsonNode messageNode = choicesNode.get(0).get("message");
                if (messageNode == null || !messageNode.has("content")) {
                    System.err.println("Missing content in GPT message.");
                    continue;
                }

                String rawJson = messageNode.get("content").asText();
                String cleanedJson = rawJson.replaceAll("```json", "").replaceAll("```", "").trim();

                JsonNode actual = mapper.readTree(cleanedJson);

                if (!actual.has("title") || !actual.has("description") || !actual.has("basedOn")) {
                    System.err.println("Missing required fields in GPT JSON.");
                    continue;
                }

                String title = actual.get("title").asText();
                String description = actual.get("description").asText();
                JsonNode basedOnArray = actual.get("basedOn");

                if (!basedOnArray.isArray()) {
                    System.err.println("basedOn field is not an array.");
                    continue;
                }

                for (JsonNode behaviorNode : basedOnArray) {
                    String behavior = behaviorNode.has("behavior")
                            ? behaviorNode.get("behavior").asText()
                            : (behaviorNode.has("pattern") ? behaviorNode.get("pattern").asText() : "Unknown");

                    Recommendation recommendation = new Recommendation();
                    recommendation.setChild(child);
                    recommendation.setTitle(title);
                    recommendation.setDescription(description);
                    recommendation.setBasedOnFlag(behavior);
                    recommendation.setSuggestedAt(LocalDateTime.now());
                    recommendation.setResolved(false);

                    recommendationRepository.save(recommendation);
                }

            } catch (Exception e) {
                System.err.println("GPT recommendation error for child " + child.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
