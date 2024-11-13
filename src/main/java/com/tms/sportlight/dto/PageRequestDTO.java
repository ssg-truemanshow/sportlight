package com.tms.sportlight.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO<T> {

    private T searchCond;
    private String keyword;

    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    private int size = 10;

    public Pageable getPageable() {
        int pageNum = page < 0 ? 1 : page - 1;
        int sizeNum = size <= 10 ? 10 : size;
        return PageRequest.of(page, size);
    }

    public Pageable getPageable(Sort sort){
        int pageNum = page < 0 ? 1 : page - 1;
        int sizeNum = size <= 10 ? 10 : size;
        return PageRequest.of(pageNum, sizeNum, sort);
    }
}
