package com.tms.sportlight.repository;

import com.tms.sportlight.domain.HostRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminHostRequestRepository extends JpaRepository<HostRequest, Integer> {
    @Query(value = "SELECT hr.host_req_id, u.login_id, u.user_nickname, u.user_name, hr.req_status, hr.host_bio, hr.certification, hr.portfolio, hr.reg_date " +
            "FROM host_request hr " +
            "JOIN user u ON hr.user_id = u.user_id", nativeQuery = true)
    List<Object[]> getAllHostRequests();
}
