package com.GrowSmart.GrowSmart.Repository;

import com.GrowSmart.GrowSmart.Entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, UUID> {
    List<Consent> findByChildId(UUID childId);
    List<Consent> findByUserId(UUID userId);
    Optional<Consent> findByChildIdAndUserId(UUID childId, UUID userId);
}

