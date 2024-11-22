package com.tms.sportlight.repository;

import com.tms.sportlight.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminUserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u.user_id, u.login_id, u.user_nickname, u.user_introduce, u.user_name, u.user_phone, u.marketing_agreement, u.personal_agreement, a.total_revenue " +
            "FROM user u " +
            "JOIN adjustment_aggregate a ON u.user_id = a.user_id " +
            "ORDER BY a.total_revenue DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTop3UsersByRevenue();

    @Query(value = "SELECT COUNT(*) FROM user u " +
            "JOIN user_roles ur ON u.user_id = ur.user_id " +
            "WHERE ur.roles IN ('USER', 'HOST') " +
            "GROUP BY u.user_id " +
            "HAVING COUNT(DISTINCT ur.roles) = 2", nativeQuery = true)
    Long countUsersWithUserAndHostRoles();

    @Query(value = "SELECT COUNT(*) FROM user u " +
            "JOIN user_roles ur ON u.user_id = ur.user_id " +
            "WHERE ur.roles = 'USER' " +
            "GROUP BY u.user_id " +
            "HAVING COUNT(DISTINCT ur.roles) = 1", nativeQuery = true)
    Long countUsersWithUserRoleOnly();

    @Query(value = "SELECT " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 10 AND 19 THEN 1 ELSE 0 END) AS teensCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 20 AND 29 THEN 1 ELSE 0 END) AS twentiesCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 30 AND 39 THEN 1 ELSE 0 END) AS thirtiesCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 40 AND 49 THEN 1 ELSE 0 END) AS fortiesCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 50 AND 59 THEN 1 ELSE 0 END) AS fiftiesCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 60 AND 69 THEN 1 ELSE 0 END) AS sixtiesCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 70 AND 79 THEN 1 ELSE 0 END) AS seventiesCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) BETWEEN 80 AND 89 THEN 1 ELSE 0 END) AS eightiesCount, " +
            "SUM(CASE WHEN YEAR(CURDATE()) - YEAR(STR_TO_DATE(u.user_birth, '%Y%m%d')) >= 90 THEN 1 ELSE 0 END) AS ninetiesCount " +
            "FROM user u", nativeQuery = true)
    Object[] getUserAgeGroupCounts();


}
