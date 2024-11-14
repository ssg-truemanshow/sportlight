package com.tms.sportlight.dto;

import com.tms.sportlight.domain.CourseLevel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateDTO {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;
    @NotEmpty
    private String content;
    @Range(min = 1000, max = 999999999)
    private double tuition;
    @Range(min = 0, max = 100)
    private double discountRate;
    @NotNull
    private CourseLevel level;
    @NotEmpty
    @Size(min = 1, max = 255)
    private String address;
    @Size(min = 1, max = 255)
    private String detailAddress;
    @DecimalMin("33.0")
    @DecimalMax("44.0")
    private double latitude;
    @DecimalMin("124.0")
    @DecimalMax("133.0")
    private double longitude;
    @Range(min = 1, max = 255)
    private int time;
    @Range(min = 1, max = 999)
    private int maxCapacity;
    @Min(0)
    private int minDaysPriorToReservation;

    private MultipartFile newMainImage;
    @Size(max = 8)
    private List<MultipartFile> newImages = new ArrayList<>();
    @Size(max = 8)
    private List<Integer> deletedImages = new ArrayList<>();
}
