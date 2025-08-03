package com.GrowSmart.GrowSmart.Controller;

import com.GrowSmart.GrowSmart.DTO.RecommendationResponseDTO;
import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.Recommendation;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.RecommendationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v0/recommendations")
public class RecommendationController {

    private final RecommendationRepository recommendationRepository;
    private final ChildRepository childRepository;

    public RecommendationController(RecommendationRepository recommendationRepository, ChildRepository childRepository) {
        this.recommendationRepository = recommendationRepository;
        this.childRepository = childRepository;
    }

    @GetMapping("/{childId}")
    public ResponseEntity<List<RecommendationResponseDTO>> getRecommendationsByChildId(@PathVariable String childId) {
        UUID childUUID;
        try {
            childUUID = UUID.fromString(childId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Invalid UUID format
        }

        List<Recommendation> recs = recommendationRepository.findLatestRecommendationsByFlagForChild(childUUID);
        List<RecommendationResponseDTO> response = recs.stream()
                .map(r -> new RecommendationResponseDTO(
                        r.getBasedOnFlag(),
                        r.getTitle(),
                        r.getDescription()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}