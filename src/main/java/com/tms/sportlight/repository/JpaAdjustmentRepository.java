package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Adjustment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaAdjustmentRepository extends JpaRepository<Adjustment, Integer> {

    List<Adjustment> findByUserIdOrderByReqDateDesc(long userId, Pageable pageable);
    int countByUserId(long userId);

    @Query(value = "SELECT IFNULL(total_revenue, 0) - (SELECT IFNULL(SUM(request_amount), 0) FROM adjustment WHERE user_id=?1 AND status<>'FAIL')\n" +
            "FROM adjustment_aggregate \n" +
            "WHERE user_id=?1", nativeQuery = true)
    double getPossibleAdjustmentAmount(long userId);

}
