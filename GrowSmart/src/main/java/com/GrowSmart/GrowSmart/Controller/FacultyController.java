package com.GrowSmart.GrowSmart.Controller;

import com.GrowSmart.GrowSmart.DTO.BehaviorLogDTO;
import com.GrowSmart.GrowSmart.DTO.ChildDTO;
import com.GrowSmart.GrowSmart.DTO.GeneralResponseDTO;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Enums.Standard;
import com.GrowSmart.GrowSmart.Mapper.ChildMapper;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.FacultyStandardAssignmentRepository;
import com.GrowSmart.GrowSmart.Repository.UserRepository;
import com.GrowSmart.GrowSmart.Service.FacultyService;
import com.GrowSmart.GrowSmart.Service.BehaviorLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v0/faculty")

public class FacultyController {

    private final BehaviorLogService behaviorLogService;
    private final UserRepository userRepository;
    private final FacultyService facultyService;
    private final ChildRepository childRepository;
    private final ChildMapper childMapper;
    private final FacultyStandardAssignmentRepository assignmentRepository;
    public FacultyController(BehaviorLogService behaviorLogService, UserRepository userRepository, FacultyService facultyService, ChildRepository childRepository, ChildMapper childMapper, FacultyStandardAssignmentRepository assignmentRepository) {
        this.behaviorLogService = behaviorLogService;
        this.userRepository = userRepository;
        this.facultyService = facultyService;
        this.childRepository = childRepository;
        this.childMapper = childMapper;
        this.assignmentRepository = assignmentRepository;
    }

    @PostMapping("/children")
    @PreAuthorize("hasAuthority('ROLE_FACULTY')")
    public ResponseEntity<GeneralResponseDTO> createChildren(@RequestBody ChildDTO dto, Principal principal) {
        String facultyEmail = principal.getName();

        boolean created = facultyService.createChild(dto, facultyEmail);  // <- pass email

        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setMessage("Child entry created successfully");
        response.setSuccess(String.valueOf(created));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ROLE_FACULTY')")
    @GetMapping("/children/{standard}")
    public ResponseEntity<List<ChildDTO>> getChildrenByStandard(@PathVariable Standard standard,
                                                                Principal principal) {
        String facultyEmail = principal.getName();

        // Verify this faculty is assigned to that standard
        boolean isAssigned = assignmentRepository.findByFacultyEmail(facultyEmail)
                .stream()
                .anyMatch(assignment -> assignment.getStandard() == standard);

        if (!isAssigned) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ChildDTO> result = childRepository.findByStandard(standard)
                .stream()
                .map(childMapper::toDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('ROLE_FACULTY')")
    @PostMapping("/behavior-log")
    public ResponseEntity<GeneralResponseDTO> createBehaviorLog(@RequestBody BehaviorLogDTO dto, Authentication authentication) {
        String email = authentication.getName();
        boolean saved = behaviorLogService.createLog(dto, email);
        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setMessage("Behavior log created successfully");
        response.setSuccess(String.valueOf(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ROLE_FACULTY')")
    @GetMapping
    public ResponseEntity<List<BehaviorLogDTO>> getLogsByChild(@RequestParam UUID childId, Authentication authentication) {
        String facultyEmail = authentication.getName();
        User facultyUser = userRepository.findByEmail(facultyEmail)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        return ResponseEntity.ok(behaviorLogService.getLogsByChild(childId, facultyUser.getId()));
    }

}
