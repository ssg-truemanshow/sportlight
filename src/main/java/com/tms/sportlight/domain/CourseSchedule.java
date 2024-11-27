package com.tms.sportlight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_schedule_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
    private boolean deleted;
    private int remainedNum;

    public void deleteCourseSchedule() {
        this.deleted = true;
        this.delDate = LocalDateTime.now();
    }

    public void updateRemainedNum(int num) {
        this.remainedNum = num;
    }

}
