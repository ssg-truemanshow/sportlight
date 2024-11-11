package com.tms.sportlight.repository;

import com.tms.sportlight.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class CourseRepositoryTest {

    @Autowired
    JpaCourseRepository courseRepository;
    @Autowired
    UserRepository userRepository;
    User host;

    @BeforeEach
    void creatorInit() {
        host = User.builder()
                .roles(List.of(UserRole.USER, UserRole.HOST))
                .termsAgreement(true)
                .regDate(LocalDateTime.now())
                .isDeleted(false)
                .build();
        userRepository.save(host);
    }

    @Test
    void save() {
        Course course = Course.builder()
                .user(host)
                .category(new Category(1))
                .title("title")
                .content("content")
                .discountRate(0.25)
                .level(CourseLevel.BEGINNER)
                .address("address")
                .detailAddress("detailAddress")
                .tuition(20000)
                .minDaysPriorToReservation(3)
                .maxCapacity(10)
                .latitude(127.0495556)
                .longitude(37.514575)
                .views(0)
                .status(CourseStatus.APPROVAL_REQUEST)
                .regDate(LocalDateTime.now())
                .build();
        courseRepository.save(course);

        Course findCourse = courseRepository.findById(course.getId()).get();
        Assertions.assertEquals(course.getTitle(), findCourse.getTitle());
        Assertions.assertEquals(course.getContent(), findCourse.getContent());
        Assertions.assertEquals(course.getDiscountRate(), findCourse.getDiscountRate());
        Assertions.assertEquals(course.getLevel(), findCourse.getLevel());
        Assertions.assertEquals(course.getAddress(), findCourse.getAddress());
        Assertions.assertEquals(course.getDetailAddress(), findCourse.getDetailAddress());
        Assertions.assertEquals(course.getTuition(), findCourse.getTuition());
        Assertions.assertEquals(course.getLatitude(), findCourse.getLatitude());
        Assertions.assertEquals(course.getLongitude(), findCourse.getLongitude());
        Assertions.assertEquals(course.getViews(), findCourse.getViews());
        Assertions.assertEquals(course.getStatus(), findCourse.getStatus());
        Assertions.assertEquals(course.getCategory().getId(), findCourse.getCategory().getId());
        Assertions.assertEquals(course.getUser().getId(), findCourse.getUser().getId());
    }

}
