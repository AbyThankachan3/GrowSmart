package com.GrowSmart.GrowSmart.Repository;

import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Enums.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChildRepository extends JpaRepository<Child, UUID> {
    Optional<Child> findByStudentCode(String studentCode);
    List<Child> findByStandard(Standard standard);

//    Child findByChild(Child child);
    List<Child> findAllByGuardian(User guardian);
    Optional<Child> findByGuardian(User parent);
}
