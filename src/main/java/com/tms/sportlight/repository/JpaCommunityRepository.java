package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCommunityRepository extends JpaRepository<Community, Integer> {

}
