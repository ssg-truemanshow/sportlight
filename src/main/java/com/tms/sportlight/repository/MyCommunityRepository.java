package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Community;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyCommunityRepository extends JpaRepository<Community, Integer> {

    @Query("SELECT c FROM Community c " +
        "JOIN CommunityParticipant uc ON c.id = uc.community.id " +
        "WHERE uc.user.id = :userId AND c.deleted = false " +
        "ORDER BY uc.enterDate DESC")
    List<Community> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Community c " +
        "JOIN CommunityParticipant uc ON c.id = uc.community.id " +
        "WHERE uc.user.id = :userId AND c.deleted = false")
    int countByUserId(@Param("userId") Long userId);
}

