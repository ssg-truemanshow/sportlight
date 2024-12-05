package com.tms.sportlight.repository;

import com.tms.sportlight.domain.HostInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HostInfoRepository extends JpaRepository<HostInfo, Integer> {

    Optional<HostInfo> findByUserId(long userId);
}
