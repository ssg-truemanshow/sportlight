package com.tms.sportlight.repository;

import com.tms.sportlight.domain.MyCouponStatus;
import com.tms.sportlight.domain.UserCoupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Query("SELECT uc FROM UserCoupon uc " +
        "WHERE uc.user.id = :userId " +
        "AND uc.isActive = true " +
        "AND uc.expDate > CURRENT_TIMESTAMP " +
        "ORDER BY uc.expDate ASC")
    List<UserCoupon> findAvailableByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(uc) FROM UserCoupon uc " +
        "WHERE uc.user.id = :userId " +
        "AND uc.isActive = true " +
        "AND uc.expDate > CURRENT_TIMESTAMP")
    int countAvailableByUserId(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserCoupon uc " +
        "WHERE uc.id = :couponId " +
        "AND uc.user.id = :userId " +
        "AND uc.isActive = true")
    Optional<UserCoupon> findByIdAndUserId(@Param("couponId") Integer couponId, @Param("userId") Long userId);

    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.id = :userId AND " +
        "((:status = 'AVAILABLE' AND uc.isActive = true AND uc.expDate > CURRENT_TIMESTAMP) OR " +
        "(:status = 'USED' AND uc.isActive = false) OR " +
        "(:status = 'EXPIRED' AND uc.isActive = true AND uc.expDate <= CURRENT_TIMESTAMP))")
    Page<UserCoupon> findAllByUserIdAndStatus(
        @Param("userId") Long userId,
        @Param("status") String status,
        Pageable pageable
    );
}
