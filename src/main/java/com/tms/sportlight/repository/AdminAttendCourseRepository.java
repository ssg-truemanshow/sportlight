package com.tms.sportlight.repository;

import com.tms.sportlight.domain.AttendCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminAttendCourseRepository extends JpaRepository<AttendCourse, Integer> {
    List<AttendCourse> findAllByRefundLogIsNull();

    @Query(value = "SELECT u.user_gender AS gender, " +
            "CASE " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 10 AND 19 THEN '10대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 20 AND 29 THEN '20대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 30 AND 39 THEN '30대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 40 AND 49 THEN '40대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 50 AND 59 THEN '50대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 60 AND 69 THEN '60대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 70 AND 79 THEN '70대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 80 AND 89 THEN '80대' " +
            "    WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) >= 90 THEN '90대' " +
            "END AS ageGroup, " +
            "COUNT(ac.attend_course_id) AS courseCount " +
            "FROM attend_course ac " +
            "JOIN user u ON ac.user_id = u.user_id " +
            "GROUP BY gender, ageGroup", nativeQuery = true)
    List<Object[]> getCourseCountsGroupedByGenderAndAge();
}
