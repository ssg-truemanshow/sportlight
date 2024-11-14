package com.tms.sportlight.service;

import com.tms.sportlight.domain.Category;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category get(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_CATEGORY));
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
