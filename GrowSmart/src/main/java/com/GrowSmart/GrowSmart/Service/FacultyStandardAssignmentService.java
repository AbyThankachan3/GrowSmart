package com.GrowSmart.GrowSmart.Service;

import com.GrowSmart.GrowSmart.DTO.FacultyStandardAssignmentDTO;
import com.GrowSmart.GrowSmart.Entity.FacultyStandardAssignment;
import com.GrowSmart.GrowSmart.Mapper.FacultyStandardAssignmentMapper;
import com.GrowSmart.GrowSmart.Repository.FacultyStandardAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FacultyStandardAssignmentService {

    private final FacultyStandardAssignmentRepository assignmentRepository;
    private final FacultyStandardAssignmentMapper mapper;

    public FacultyStandardAssignmentService(FacultyStandardAssignmentRepository assignmentRepository,
                                            FacultyStandardAssignmentMapper mapper) {
        this.assignmentRepository = assignmentRepository;
        this.mapper = mapper;
    }

    @Transactional
    public boolean create(FacultyStandardAssignmentDTO dto) {
        FacultyStandardAssignment entity = mapper.toEntity(dto);
        assignmentRepository.save(entity);
        return true;
    }

    public List<FacultyStandardAssignmentDTO> getAll() {
        return assignmentRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public void delete(UUID id) {
        assignmentRepository.deleteById(id);
    }
}
