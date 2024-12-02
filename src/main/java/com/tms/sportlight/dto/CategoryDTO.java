package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Integer id;
    private String name;

    public static CategoryDTO fromEntity(Category category) {
        return CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }
}