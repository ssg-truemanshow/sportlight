package com.tms.sportlight.repository;

import com.tms.sportlight.domain.CommunityParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCommunityRepository {

    private final JpaUserCommunityRepository jpaUserCommunityRepository;

    public boolean existsByUserIdAndCommunityId(long userId, int communityId) {
        return jpaUserCommunityRepository.existsByUserIdAndCommunityId(userId, communityId);
    }

    public List<CommunityParticipant> findByCommunityId(int communityId) {
        return jpaUserCommunityRepository.findByCommunityId(communityId);
    }

    public int countByCommunityId(int communityId) {
        return jpaUserCommunityRepository.countByCommunityId(communityId);
    }

    public void deleteByUserIdAndCommunityId(long userId, int communityId) {
        jpaUserCommunityRepository.deleteByUserIdAndCommunityId(userId, communityId);
    }

    public void deleteByCommunityId(int communityId) {
        jpaUserCommunityRepository.deleteByCommunityId(communityId);
    }

    public void save(CommunityParticipant communityParticipant) {
        jpaUserCommunityRepository.save(communityParticipant);
    }

    public boolean existsByUsernameAndCommunityId(String username, int communityId) {
        return jpaUserCommunityRepository.existsByUsernameAndCommunityId(username, communityId);
    }
}
