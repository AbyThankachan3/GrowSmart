package com.GrowSmart.GrowSmart.Repository;

import com.GrowSmart.GrowSmart.Entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, UUID> {
    List<Observation> findByChildId(UUID childId);
}

