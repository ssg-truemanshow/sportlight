package com.tms.sportlight.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySearchDTO {

    @Positive
    private Integer categoryId;
    @DecimalMin("33.0")
    @DecimalMax("44.0")
    private double minLatitude;
    @DecimalMin("33.0")
    @DecimalMax("44.0")
    private double maxLatitude;
    @DecimalMin("124.0")
    @DecimalMax("133.0")
    private double minLongitude;
    @DecimalMin("124.0")
    @DecimalMax("133.0")
    private double maxLongitude;
    @DecimalMin("33.0")
    @DecimalMax("44.0")
    private double refLatitude;
    @DecimalMin("124.0")
    @DecimalMax("133.0")
    private double refLongitude;
    private SortType sortType;

}
