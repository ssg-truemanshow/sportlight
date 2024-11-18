package com.tms.sportlight.repository;

import com.tms.sportlight.domain.CommunityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUserCommunityRepository extends JpaRepository<CommunityParticipant, Integer> {

    boolean existsByUserIdAndCommunityId(long userId, int communityId);
    List<CommunityParticipant> findByCommunityId(int communityId);
    int countByCommunityId(int communityId);
    void deleteByUserIdAndCommunityId(long userId, int communityId);
    void deleteByCommunityId(int communityId);

}
