package com.GrowSmart.GrowSmart.Controller;

import com.GrowSmart.GrowSmart.DTO.BehaviorLogDTO;
import com.GrowSmart.GrowSmart.DTO.GeneralResponseDTO;
import com.GrowSmart.GrowSmart.DTO.ParentsChildResponse;
import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.UserRepository;
import com.GrowSmart.GrowSmart.Service.BehaviorLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v0/parents")
public class ParentsController {

    private final BehaviorLogService behaviorLogService;
    private final ChildRepository childRepository;
    private final UserRepository userRepository;

    public ParentsController(BehaviorLogService behaviorLogService, ChildRepository childRepository, UserRepository userRepository) {
        this.behaviorLogService = behaviorLogService;
        this.childRepository = childRepository;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ROLE_PARENTS')")
    @PostMapping("/behavior-log")
    public ResponseEntity<GeneralResponseDTO> logBehavior(@RequestBody BehaviorLogDTO dto, Principal principal) {
        String parentEmail = principal.getName();
            boolean success = behaviorLogService.createLog(dto, parentEmail);
            GeneralResponseDTO response = new GeneralResponseDTO();
            response.setSuccess(String.valueOf(success));
            if (success) {
                response.setMessage("Behavior log created successfully");
                return ResponseEntity.ok(response);
            } else {
                response.setMessage("Behavior log creation failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
    }

    @PreAuthorize("hasRole('ROLE_PARENTS')")
    @GetMapping("/child")
    public ResponseEntity<List<ParentsChildResponse>> getChildren(Principal principal) {
        String parentEmail = principal.getName();
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new RuntimeException("Parent not found: " + parentEmail));

        List<Child> children = childRepository.findAllByGuardian(parent);

        List<ParentsChildResponse> response = children.stream().map(child -> {
            ParentsChildResponse dto = new ParentsChildResponse();
            dto.setChildId(child.getId());
            dto.setFullName(child.getFullName());
            dto.setDateOfBirth(child.getDateOfBirth());
            dto.setStandard(child.getStandard());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }
}