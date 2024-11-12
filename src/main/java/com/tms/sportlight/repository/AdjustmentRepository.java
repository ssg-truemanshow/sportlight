package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Adjustment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdjustmentRepository {

    private final JpaAdjustRepository jpaAdjustRepository;

    public int save(Adjustment adjustment) {
        return jpaAdjustRepository.save(adjustment).getId();
    }

    public Optional<Adjustment> findById(int id) {
        return jpaAdjustRepository.findById(id);
    }

    public List<Adjustment> findByUserId(long userId) {
        return jpaAdjustRepository.findByUserId(userId);
    }
}
