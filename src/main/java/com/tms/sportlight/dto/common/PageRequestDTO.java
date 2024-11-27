package com.tms.sportlight.dto.common;

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
        int pageNum = getPageNum(page);
        int sizeNum = getSizeNum(size);
        return PageRequest.of(pageNum, sizeNum);
    }

    public Pageable getPageable(Sort sort){
        int pageNum = getPageNum(page);
        int sizeNum = getSizeNum(size);
        return PageRequest.of(pageNum, sizeNum, sort);
    }

    public int getSkip(){
        return (page - 1) * size;
    }

    private int getPageNum(int page) {
        return page < 0 ? 1 : page - 1;
    }

    private int getSizeNum(int size) {
        return Math.max(size, 10);
    }
}
