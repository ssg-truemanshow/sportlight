package com.tms.sportlight.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInterestsRequestDTO {

    @Size(max = 5, message = "관심 분야는 최대 5개까지 설정 가능합니다.")
    private List<Integer> categoryIds;

}
