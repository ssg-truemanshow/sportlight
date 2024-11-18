package com.tms.sportlight.repository;

import com.tms.sportlight.domain.MonthlySale;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface MonthlySaleRepository extends JpaRepository<MonthlySale, Integer> {
    @Query("SELECT COALESCE(AVG(m.totalAmount), 0) FROM MonthlySale m")
    BigDecimal findAverageMonthlySaleAmount();
}
