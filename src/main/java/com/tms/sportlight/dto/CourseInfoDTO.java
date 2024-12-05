package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfoDTO {

    private String title;
    private int categoryId;
    private String content;
    private Double tuition;
    private Double discountRate;
    private CourseLevel level;
    private String address;
    private String detailAddress;
    private double latitude;
    private double longitude;
    private int time;
    private int maxCapacity;
    private int minDaysPriorToReservation;
    private FileDTO existMainImage;
    private List<FileDTO> existImages = new ArrayList<>();

    @Builder(builderMethodName = "create")
    public CourseInfoDTO(Course course, FileDTO existMainImage, List<FileDTO> existImages) {
        this.title = course.getTitle();
        this.categoryId = course.getCategory().getId();
        this.content = course.getContent();
        this.tuition = course.getTuition();
        this.discountRate = course.getDiscountRate();
        this.level = course.getLevel();
        this.address = course.getAddress();
        this.detailAddress = course.getDetailAddress();
        this.latitude = course.getLatitude();
        this.longitude = course.getLongitude();
        this.time = course.getTime();
        this.maxCapacity = course.getMaxCapacity();
        this.minDaysPriorToReservation = course.getMinDaysPriorToReservation();
        this.existMainImage = existMainImage;
        this.existImages = existImages;
    }
}
