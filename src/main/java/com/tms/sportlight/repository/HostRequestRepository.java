package com.tms.sportlight.repository;

import com.tms.sportlight.domain.HostRequest;
import com.tms.sportlight.domain.HostRequestStatus;
import com.tms.sportlight.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HostRequestRepository extends JpaRepository<HostRequest, Integer> {

    List<HostRequest> findByReqStatus(HostRequestStatus reqStatus);

    Optional<HostRequest> findByUser(User user);
}
