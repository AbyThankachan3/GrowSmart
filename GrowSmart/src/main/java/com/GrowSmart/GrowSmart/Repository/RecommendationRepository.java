package com.GrowSmart.GrowSmart.Repository;

import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {
    List<Recommendation> findByChildIdAndResolvedFalse(UUID childId);

    List<Recommendation> findByChild(Child child);
    @Query(value =
        """
        SELECT r.* FROM recommendations r
        INNER JOIN (
            SELECT based_on_flag, MAX(suggested_at) AS latest
            FROM recommendations
            WHERE child_id = :childId
            GROUP BY based_on_flag
        ) latest_per_flag
        ON r.based_on_flag = latest_per_flag.based_on_flag
        AND r.suggested_at = latest_per_flag.latest
        WHERE r.child_id = :childId
        """,
            nativeQuery = true)

    List<Recommendation> findLatestRecommendationsByFlagForChild(@Param("childId") UUID childId);
}

