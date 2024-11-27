package com.tms.sportlight.repository;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserCouponRepository extends JpaRepository<UserCoupon, Integer> {

}
