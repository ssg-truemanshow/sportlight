package com.tms.sportlight.repository;

import com.tms.sportlight.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByUserNameAndUserPhone(String userName, String userPhone);

}
