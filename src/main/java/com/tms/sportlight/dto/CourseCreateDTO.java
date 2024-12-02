package com.tms.sportlight.dto;

import com.tms.sportlight.domain.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateDTO {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;
    @NotNull
    @Positive
    private Integer categoryId;
    @NotEmpty
    @Size(min = 1, max = 10000)
    private String content;
    @NotNull
    @Range(min = 1000, max = 999999999)
    private Double tuition;
    @NotNull
    @Range(min = 0, max = 100)
    private Double discountRate;
    @NotNull
    private CourseLevel level;
    @NotEmpty
    @Size(min = 1, max = 255)
    private String address;
    @Size(min = 1, max = 255)
    private String detailAddress;
    @NotNull
    @DecimalMin("33.0")
    @DecimalMax("44.0")
    private Double latitude;
    @NotNull
    @DecimalMin("124.0")
    @DecimalMax("133.0")
    private Double longitude;
    @NotNull
    @Range(min = 1, max = 255)
    private Integer time;
    @NotNull
    @Range(min = 1, max = 999)
    private Integer maxCapacity;
    @NotNull
    @Min(0)
    private Integer minDaysPriorToReservation;
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
            .discountRate(discountRate)
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
