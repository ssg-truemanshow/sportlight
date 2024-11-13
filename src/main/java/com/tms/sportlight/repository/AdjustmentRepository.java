package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Adjustment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdjustmentRepository {

    private final JpaAdjustmentRepository jpaAdjustmentRepository;

    public int save(Adjustment adjustment) {
        return jpaAdjustmentRepository.save(adjustment).getId();
    }

    public Optional<Adjustment> findById(int id) {
        return jpaAdjustmentRepository.findById(id);
    }

    public List<Adjustment> findByUserId(long userId, Pageable pageable) {
        return jpaAdjustmentRepository.findByUserIdOrderByReqDateDesc(userId, pageable);
    }

    public int findCountByUserId(long userId) {
        return jpaAdjustmentRepository.countByUserId(userId);
    }
}
