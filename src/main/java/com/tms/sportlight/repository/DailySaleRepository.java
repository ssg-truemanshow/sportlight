package com.tms.sportlight.repository;

import com.tms.sportlight.domain.DailySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DailySaleRepository extends JpaRepository<DailySale, Integer> {
//    @Query(value = "SELECT COALESCE(AVG(d.totalAmount), 0) FROM DailySale d", nativeQuery = true)
    @Query("SELECT COALESCE(AVG(d.totalAmount), 0) FROM DailySale d")
    BigDecimal findAverageDailySaleAmount();

    @Query("SELECT COALESCE(SUM(d.totalAmount), 0) FROM DailySale d")
    BigDecimal findTotalDailySaleAmount();
}
