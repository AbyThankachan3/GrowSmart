package com.GrowSmart.GrowSmart.Repository;

import com.GrowSmart.GrowSmart.Entity.GrowthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GrowthRecordRepository extends JpaRepository<GrowthRecord, UUID> {
    List<GrowthRecord> findByChildIdOrderByRecordedAtDesc(UUID childId);
}
