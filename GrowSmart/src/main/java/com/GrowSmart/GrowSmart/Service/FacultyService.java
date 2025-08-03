package com.GrowSmart.GrowSmart.Service;

import com.GrowSmart.GrowSmart.DTO.ChildDTO;
import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Enums.Role;
import com.GrowSmart.GrowSmart.Mapper.ChildMapper;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.FacultyStandardAssignmentRepository;
import com.GrowSmart.GrowSmart.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class FacultyService {

    private final FacultyStandardAssignmentRepository facultyStandardAssignmentRepository;
    private final UserRepository userRepository;
    private final ChildMapper childMapper;
    private final ChildRepository childRepository;
    public FacultyService(FacultyStandardAssignmentRepository facultyStandardAssignmentRepository, UserRepository userRepository, ChildMapper childMapper, ChildRepository childRepository) {
        this.facultyStandardAssignmentRepository = facultyStandardAssignmentRepository;
        this.userRepository = userRepository;
        this.childMapper = childMapper;
        this.childRepository = childRepository;
    }

    @Transactional
    public boolean createChild(ChildDTO dto, String facultyEmail) {
        // 1. Check if guardian exists and has role PARENTS
        User guardian = userRepository.findByEmail(dto.getGuardianEmail())
                .filter(user -> user.getRole() == Role.PARENTS)
                .orElseThrow(() -> new IllegalArgumentException("Guardian not found or not a valid role"));

        // 2. Check if the faculty is assigned to the given standard
        User faculty = userRepository.findByEmail(facultyEmail)
                .filter(user -> user.getRole() == Role.FACULTY)
                .orElseThrow(() -> new IllegalArgumentException("Unauthorized access"));

        boolean isAllowed = facultyStandardAssignmentRepository.existsByFacultyAndStandard(faculty, dto.getStandard());

        if (!isAllowed) {
            throw new AccessDeniedException("Faculty is not assigned to this standard");
        }

        // 3. Create the child
        Child child = childMapper.toEntity(dto);
        child.setGuardian(guardian);

        childRepository.save(child);
        return true;
    }

}
