package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Adjustment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAdjustmentRepository extends JpaRepository<Adjustment, Integer> {

    List<Adjustment> findByUserIdOrderByReqDateDesc(long userId, Pageable pageable);
    int countByUserId(long userId);
}
