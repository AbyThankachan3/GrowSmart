package com.GrowSmart.GrowSmart.Controller;

import com.GrowSmart.GrowSmart.DTO.FacultyStandardAssignmentDTO;
import com.GrowSmart.GrowSmart.DTO.GeneralResponseDTO;
import com.GrowSmart.GrowSmart.Service.FacultyStandardAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v0/admin/faculty-assignments")
public class FacultyStandardAssignmentController {

    private final FacultyStandardAssignmentService service;

    public FacultyStandardAssignmentController(FacultyStandardAssignmentService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<GeneralResponseDTO> assignFaculty(@RequestBody FacultyStandardAssignmentDTO dto) {
        boolean created = service.create(dto);
        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(String.valueOf(created));
        response.setMessage(created ? "Faculty assignment created successfully" : "Faculty assignment already exists");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<FacultyStandardAssignmentDTO> getAllAssignments() {
        return service.getAll();
    }

}
