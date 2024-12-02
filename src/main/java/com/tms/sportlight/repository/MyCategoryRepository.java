package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyCategoryRepository extends JpaRepository<Category, Integer> {
}
