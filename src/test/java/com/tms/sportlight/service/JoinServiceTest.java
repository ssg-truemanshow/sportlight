package com.tms.sportlight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.dto.JoinDTO;
import com.tms.sportlight.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class JoinServiceTest {

    @Autowired
    private JoinService joinService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private JoinDTO joinInfo1;
    private JoinDTO joinInfo2;

    @BeforeEach
    public void setup1() {
        joinInfo1 = JoinDTO.builder()
            .loginId("xoghks211@naver.com")
            .loginPwd("password12")
            .userNickname("TH")
            .userName("누구개")
            .userGender("Male")
            .userBirth("19960211")
            .userPhone("01012345678")
            .termsAgreement(true)
            .marketingAgreement(false)
            .personalAgreement(true)
            .joinMethod(null)
            .isDeleted(false)
            .build();
    }

    @BeforeEach
    public void setup2() {
        joinInfo2 = JoinDTO.builder()
            .loginId("username")
            .loginPwd("password")
            .userNickname("TH")
            .userName("누구개")
            .userGender("Male")
            .userBirth("19960211")
            .userPhone("01012345678")
            .termsAgreement(true)
            .marketingAgreement(false)
            .personalAgreement(true)
            .joinMethod(null)
            .isDeleted(false)
            .build();
    }

    @Test
    public void 회원가입() {
        // when
        joinService.joinProcess(joinInfo1);

        // then
        User savedUser = userRepository.findByLoginId("xoghks211@naver.com")
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        assertEquals("xoghks211@naver.com", savedUser.getLoginId());
        assertTrue(bCryptPasswordEncoder.matches("password12", savedUser.getLoginPwd()));
        assertEquals("TH", savedUser.getUserNickname());
        assertEquals("누구개", savedUser.getUserName());
        assertEquals("Male", savedUser.getUserGender());
        assertEquals("19960211", savedUser.getUserBirth());
        assertEquals("01012345678", savedUser.getUserPhone());
        assertTrue(savedUser.getTermsAgreement());
        assertFalse(savedUser.getMarketingAgreement());
        assertTrue(savedUser.getPersonalAgreement());
        assertEquals(null, savedUser.getJoinMethod());
        assertEquals(List.of(UserRole.USER), savedUser.getRoles());
        assertFalse(savedUser.getIsDeleted());
    }

    @Test
    public void 이메일형식_검증() {
        // when
        joinService.joinProcess(joinInfo2);
    }
}
