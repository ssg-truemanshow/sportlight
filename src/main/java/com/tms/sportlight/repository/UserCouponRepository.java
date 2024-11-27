package com.tms.sportlight.repository;

import com.tms.sportlight.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer> {
}
