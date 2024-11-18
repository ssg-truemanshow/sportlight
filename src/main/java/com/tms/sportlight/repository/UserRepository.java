package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Review;
import com.tms.sportlight.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);

    boolean existsByUserNickname(String userNickname);

    Optional<User> findByLoginId(String loginId);

}
