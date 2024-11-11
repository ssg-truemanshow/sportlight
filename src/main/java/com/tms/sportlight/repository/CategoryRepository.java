package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;

    public Optional<Category> findById(int id) {
        return jpaCategoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return jpaCategoryRepository.findAll();
    }
 }
