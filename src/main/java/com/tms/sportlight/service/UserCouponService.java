package com.tms.sportlight.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.AvailableCouponDTO;
import com.tms.sportlight.repository.UserCouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCouponService {

  private final UserCouponRepository userCouponRepository;

  @Transactional(readOnly = true)
  public List<AvailableCouponDTO> getAvailableCouponByUser(User user) {
    return userCouponRepository.findByUser(user);
  }
}
