package com.GrowSmart.GrowSmart.Repository;

import com.GrowSmart.GrowSmart.Entity.FacultyStandardAssignment;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Enums.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FacultyStandardAssignmentRepository extends JpaRepository<FacultyStandardAssignment, UUID> {
    List<FacultyStandardAssignment> findByFacultyEmail(String email);
    boolean existsByFacultyAndStandard(User faculty, Standard standard);
}