package com.tms.sportlight.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.QCoupon;
import com.tms.sportlight.domain.QUserCoupon;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserCoupon;
import com.tms.sportlight.dto.AvailableCouponDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponRepository {
  private final JpaUserCouponRepository jpaUserCouponRepository;
  private final JPAQueryFactory queryFactory;

  public Optional<UserCoupon> findById(Integer userCouponId) {
    return jpaUserCouponRepository.findById(userCouponId);
  }

  public List<AvailableCouponDTO> findByUser(User user) {
    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    QCoupon coupon = QCoupon.coupon;


    BooleanBuilder whereClause = new BooleanBuilder();

    whereClause.and(userCoupon.user.eq(user));
    whereClause.and(userCoupon.expDate.goe(LocalDateTime.now()));
    whereClause.and(userCoupon.isActive.isTrue());

    return queryFactory.select(
            Projections.constructor(AvailableCouponDTO.class,
                userCoupon.id,
                userCoupon.issDate,
                userCoupon.expDate,
                coupon.name,
                coupon.discountRate
            ))
        .from(userCoupon)
        .leftJoin(coupon).on(userCoupon.coupon.eq(coupon))
        .where(whereClause)
        .orderBy(userCoupon.issDate.desc())
        .fetch();
  }
}
