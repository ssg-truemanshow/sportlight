package com.tms.sportlight.repository;

import com.tms.sportlight.domain.RefundLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRefundRepository extends JpaRepository<RefundLog, Integer> {
}
