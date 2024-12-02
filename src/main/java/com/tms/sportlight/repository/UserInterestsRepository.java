package com.tms.sportlight.repository;

import com.tms.sportlight.domain.UserInterestId;
import com.tms.sportlight.domain.UserInterests;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestsRepository extends JpaRepository<UserInterests, UserInterestId> {

    List<UserInterests> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    int countByUserId(Long userId);

}
