package com.tms.sportlight.facade;

import com.tms.sportlight.domain.UserCoupon;
import com.tms.sportlight.repository.AdminUserRepository;
import com.tms.sportlight.repository.CouponRepository;
import com.tms.sportlight.repository.EventRepository;
import com.tms.sportlight.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
@Log
public class RedissonLockUserCouponFacade {
    private final RedissonClient redissonClient;
    private final UserCouponRepository userCouponRepository;
    private final EventRepository eventRepository;
    private final AdminUserRepository adminUserRepository;
    private final CouponRepository couponRepository;

    public void lockCoupon(Long userId, Integer eventId, Integer couponId) {
        RLock lock = redissonClient.getLock("event-lock:" + eventId);

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                log.info("lock 획득 실패");
                return;
            }

            var event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없습니다."));

            if (event.getRemainedNum() <= 0) {
                throw new RuntimeException("쿠폰이 모두 소진되었습니다.");
            }

            UserCoupon userCoupon = UserCoupon.builder()
                    .user(adminUserRepository.findById(userId).get())
                    .coupon(couponRepository.findById(couponId).get())
                    .issDate(LocalDateTime.now())
                    .isActive(false)
                    .expDate(event.getEndDate())
                    .build();

            userCouponRepository.save(userCoupon);

            event.setRemainedNum(event.getRemainedNum() - 1);
            eventRepository.save(event);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
