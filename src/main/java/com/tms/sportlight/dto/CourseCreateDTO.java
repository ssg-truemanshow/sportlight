package com.tms.sportlight.dto;

import com.tms.sportlight.domain.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateDTO {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;
    @Positive
    private int categoryId;
    @NotEmpty
    @Size(min = 1, max = 10000)
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
    @NotNull
    private MultipartFile mainImage;
    @Size(max = 8)
    private List<MultipartFile> images = new ArrayList<>();

    public Course toEntity(User user, Category category) {
        return Course.builder()
                .category(category)
                .user(user)
                .title(title)
                .content(content)
                .tuition(tuition)
                .discountRate(discountRate / 100.0)
                .level(level)
                .address(address)
                .detailAddress(detailAddress)
                .latitude(latitude)
                .longitude(longitude)
                .time(time)
                .maxCapacity(maxCapacity)
                .minDaysPriorToReservation(minDaysPriorToReservation)
                .views(0)
                .regDate(LocalDateTime.now())
                .status(CourseStatus.APPROVAL_REQUEST)
                .build();
    }
}
