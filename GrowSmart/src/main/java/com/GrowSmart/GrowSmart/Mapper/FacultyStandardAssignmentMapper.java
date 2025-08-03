package com.GrowSmart.GrowSmart.Mapper;

import com.GrowSmart.GrowSmart.DTO.FacultyStandardAssignmentDTO;
import com.GrowSmart.GrowSmart.Entity.FacultyStandardAssignment;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FacultyStandardAssignmentMapper {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public FacultyStandardAssignmentMapper(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;

        modelMapper.typeMap(FacultyStandardAssignment.class, FacultyStandardAssignmentDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getFaculty().getEmail(), FacultyStandardAssignmentDTO::setFacultyEmail));

        modelMapper.typeMap(FacultyStandardAssignmentDTO.class, FacultyStandardAssignment.class)
                .addMappings(mapper -> mapper.skip(FacultyStandardAssignment::setFaculty));
    }

    public FacultyStandardAssignment toEntity(FacultyStandardAssignmentDTO dto) {
        FacultyStandardAssignment entity = modelMapper.map(dto, FacultyStandardAssignment.class);
        if (dto.getFacultyEmail() != null) {
            User faculty = userRepository.findByEmail(dto.getFacultyEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Faculty not found"));
            entity.setFaculty(faculty);
        }
        return entity;
    }

    public FacultyStandardAssignmentDTO toDTO(FacultyStandardAssignment entity) {
        return modelMapper.map(entity, FacultyStandardAssignmentDTO.class);
    }
}
