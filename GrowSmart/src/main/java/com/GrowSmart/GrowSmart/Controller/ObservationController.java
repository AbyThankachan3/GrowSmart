package com.GrowSmart.GrowSmart.Controller;

import com.GrowSmart.GrowSmart.DTO.PatternDTO;
import com.GrowSmart.GrowSmart.Entity.Observation;
import com.GrowSmart.GrowSmart.Repository.ObservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v0/patterns")
public class ObservationController {

    private final ObservationRepository observationRepository;

    public ObservationController(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @GetMapping("/{childId}")
    public ResponseEntity<List<PatternDTO>> getPatternsForChild(@PathVariable String childId) {
        UUID childUuid = UUID.fromString(childId); // Convert string to UUID
        List<Observation> observations = observationRepository.findByChildId(childUuid);

        List<PatternDTO> result = observations.stream()
                .map(obs -> new PatternDTO(obs.getConfidence(), obs.getPatternName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
