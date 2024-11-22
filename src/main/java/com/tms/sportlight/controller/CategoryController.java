package com.tms.sportlight.controller;

import com.tms.sportlight.domain.Category;
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
  public List<String> getAllCategories() {
    return categoryService.getAll().stream().map(Category::getName).toList();
  }
}
