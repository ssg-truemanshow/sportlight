package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Review;
import com.tms.sportlight.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);

    boolean existsByUserNickname(String userNickname);

    Optional<User> findByLoginId(String loginId);

    @Query("SELECT u.loginId FROM User u WHERE u.userName = :userName AND u.userPhone = :userPhone AND u.isDeleted = false")
    List<String> findAllLoginIds(@Param("userName") String userName, @Param("userPhone") String userPhone);

}
