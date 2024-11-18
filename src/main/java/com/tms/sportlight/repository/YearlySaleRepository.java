package com.tms.sportlight.repository;

import com.tms.sportlight.domain.YearlySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface YearlySaleRepository extends JpaRepository<YearlySale, Integer> {
    @Query("SELECT COALESCE(AVG(y.totalAmount), 0) FROM YearlySale y")
    BigDecimal findAverageYearlyAmount();
}
