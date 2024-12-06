package com.tms.sportlight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "course_title")
    private String title;

    @Column(name = "course_content")
    private String content;

    @Column(name = "course_tuition")
    private double tuition;

    @Column(name = "course_discount_rate")
    private Double discountRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_level")
    private CourseLevel level;

    @Column(name = "course_address")
    private String address;

    @Column(name = "course_detail_address")
    private String detailAddress;

    @Column(name = "course_latitude")
    private double latitude;

    @Column(name = "course_longitude")
    private double longitude;

    @Column(name = "course_time")
    private int time;

    private int maxCapacity;
    private int minDaysPriorToReservation;
    private int views;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private LocalDateTime approvedDate;

    public void updateCourse(String title, String content, double tuition, double discountRate, CourseLevel level, String address, String detailAddress, double latitude, double longitude, int time, int maxCapacity, int minDaysPriorToReservation) {
        if(Objects.nonNull(title)) {
            this.title = title;
        }
        if(Objects.nonNull(content)) {
            this.content = content;
        }
        this.tuition = tuition;
        this.discountRate = discountRate;
        if(Objects.nonNull(level)) {
            this.level = level;
        }
        if(Objects.nonNull(address)) {
            this.address = address;
        }
        if(Objects.nonNull(detailAddress)) {
            this.detailAddress = detailAddress;
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this. maxCapacity = maxCapacity;
        this.minDaysPriorToReservation = minDaysPriorToReservation;
        this.modDate = LocalDateTime.now();
    }

    public void updateStatus(CourseStatus status) {
        if(Objects.nonNull(status)) {
            this.status = status;
        }
    }

}
