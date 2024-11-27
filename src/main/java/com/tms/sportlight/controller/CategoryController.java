package com.tms.sportlight.controller;

import com.tms.sportlight.domain.Category;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/categories")
  public DataResponse<List<Category>> getAllCategories() {
    return DataResponse.of(categoryService.getAll());
  }
}
