package com.GrowSmart.GrowSmart.Repository;

import com.GrowSmart.GrowSmart.Entity.BehaviorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BehaviorLogRepository extends JpaRepository<BehaviorLog, UUID> {
    List<BehaviorLog> findByChildIdOrderByTimestampDesc(UUID childId);
    List<BehaviorLog> findByChildId(UUID childId);
    List<BehaviorLog> findByChild_IdAndLoggedBy_Id(UUID childId, UUID loggedBy);
}
