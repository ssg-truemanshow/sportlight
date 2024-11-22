package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminAdjustmentRepository extends JpaRepository<Adjustment, Integer> {
    @Query(value = "SELECT a.adjustment_id, a.user_id, a.request_amount, a.adjusted_charge, a.total_amount, a.req_date " +
            "FROM adjustment a", nativeQuery = true)
    List<Object[]> getAllAdjustments();
}
