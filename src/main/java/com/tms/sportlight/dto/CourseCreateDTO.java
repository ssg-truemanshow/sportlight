package com.tms.sportlight.dto;

import com.tms.sportlight.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateDTO {

    private String title;
    private int categoryId;
    private String content;
    private double tuition;
    private double discountRate;
    private CourseLevel level;
    private String address;
    private String detailAddress;
    private double latitude;
    private double longitude;
    private int time;
    private int maxCapacity;
    private int minDaysPriorToReservation;
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
