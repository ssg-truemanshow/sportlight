package com.tms.sportlight.repository;

import com.tms.sportlight.domain.CommunityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUserCommunityRepository extends JpaRepository<CommunityParticipant, Integer> {

    boolean existsByUserIdAndCommunityId(long userId, int communityId);
    List<CommunityParticipant> findByCommunityId(int communityId);
    int countByCommunityId(int communityId);
    void deleteByUserIdAndCommunityId(long userId, int communityId);
    void deleteByCommunityId(int communityId);

    @Query("SELECT COUNT(cp.id) = 1 FROM CommunityParticipant cp WHERE cp.user.loginId=:username AND cp.community.id=:communityId")
    boolean existsByUsernameAndCommunityId(String username, int communityId);
}
