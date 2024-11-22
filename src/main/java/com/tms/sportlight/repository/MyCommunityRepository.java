/*
package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Community;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyCommunityRepository extends JpaRepository<Community, Integer> {

    @Query(
        value = "SELECT * FROM community WHERE creator_id = ?1",
        nativeQuery = true
    )
    List<Community> findCreatedCommunities(Long creatorId);

    @Query(
        value = "SELECT c.* " +
            "FROM community c " +
            "JOIN community_participant cp ON c.community_id = cp.chat_id " +
            "WHERE cp.user_id = ?1 AND c.creator_id != ?1",
        nativeQuery = true
    )
    List<Community> findJoinedCommunities(Long userId);
}
}
*/
