package com.GrowSmart.GrowSmart.Service;

import com.GrowSmart.GrowSmart.DTO.BehaviorLogDTO;
import com.GrowSmart.GrowSmart.Entity.BehaviorLog;
import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Enums.Role;
import com.GrowSmart.GrowSmart.Enums.SourceType;
import com.GrowSmart.GrowSmart.Mapper.BehaviorLogMapper;
import com.GrowSmart.GrowSmart.Repository.BehaviorLogRepository;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BehaviorLogService {
    private final BehaviorLogRepository behaviorLogRepository;
    private final ChildRepository childRepository;
    private final UserRepository userRepository;
    private final BehaviorLogMapper behaviorLogMapper;
    private final AzureOpenAIService azureOpenAIService;
    public BehaviorLogService(BehaviorLogRepository behaviorLogRepository, ChildRepository childRepository, UserRepository userRepository, BehaviorLogMapper behaviorLogMapper, AzureOpenAIService azureOpenAIService) {
        this.behaviorLogRepository = behaviorLogRepository;
        this.childRepository = childRepository;
        this.userRepository = userRepository;
        this.behaviorLogMapper = behaviorLogMapper;
        this.azureOpenAIService = azureOpenAIService;
    }

    @Transactional
    public boolean createLog(BehaviorLogDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Child child = childRepository.findById(UUID.fromString(dto.getChildId()))
                .orElseThrow(() -> new EntityNotFoundException("Child not found"));

        BehaviorLog log = behaviorLogMapper.toEntity(dto);
        log.setChild(child);
        log.setLoggedBy(user);
        log.setTimestamp(LocalDateTime.now());
        if(user.getRole() == Role.FACULTY) {
            log.setSource(SourceType.FACULTY);
        } else if(user.getRole() == Role.PARENTS) {
            log.setSource(SourceType.PARENTS);
        } else {
            log.setSource(SourceType.AI);
        }

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are an AI assistant trained to extract emotional states or emotion-related keywords based on child behavioral descriptions. Respond only with 5 keywords on which you are most confident."),
                Map.of("role", "user", "content", dto.getEventContext()+dto.getRawText())
        );

        String responseJson = azureOpenAIService.chat(messages, 50, false);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseJson);
            String assistantReply = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            List<String> emotionList = Arrays.stream(assistantReply.split("[,\\n]"))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toList());

            log.setEmotionDetected(emotionList);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GPT emotion extraction", e);
        }

        behaviorLogRepository.save(log);

        return true;
    }

    public List<BehaviorLogDTO> getLogsByChild(UUID childId, UUID loggedBy) {
        List<BehaviorLog> logs = behaviorLogRepository.findByChild_IdAndLoggedBy_Id(childId, loggedBy);
        return logs.stream().map(behaviorLogMapper::toDTO).collect(Collectors.toList());
    }

}
