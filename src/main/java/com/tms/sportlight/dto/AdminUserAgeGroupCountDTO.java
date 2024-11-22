package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserAgeGroupCountDTO {
    private Long teensCount;        // 10대
    private Long twentiesCount;     // 20대
    private Long thirtiesCount;     // 30대
    private Long fortiesCount;      // 40대
    private Long fiftiesCount;      // 50대
    private Long sixtiesCount;      // 60대
    private Long seventiesCount;    // 70대
    private Long eightiesCount;     // 80대
    private Long ninetiesCount;     // 90대
}
