package com.tms.sportlight.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.dto.JoinDTO;
import com.tms.sportlight.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRoleServiceTest {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private UserRepository userRepository;

    private JoinDTO joinInfo;

    @BeforeEach
    public void setup() {
        joinInfo = JoinDTO.builder()
            .loginId("xoghks211@naver.com")
            .loginPwd("password12")
            .userNickname("TH")
            .userName("박태환")
            .userGender("Male")
            .userBirth("19960211")
            .userPhone("01012345678")
            .termsAgreement(true)
            .marketingAgreement(false)
            .personalAgreement(true)
            .joinMethod(null)
            .isDeleted(false)
            .build();

        userRoleService.addAdminRole(joinInfo.getLoginId());
    }

    @Test
    public void 강사권한_부여() {
        //when
        userRoleService.addHostRole(joinInfo.getLoginId());

        //then
        User savedUser = userRepository.findByLoginId(joinInfo.getLoginId())
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        assertTrue(savedUser.getRoles().contains(UserRole.HOST));
        assertTrue(savedUser.getRoles().contains(UserRole.COMMUNITY_CREATOR));
    }

    @Test
    public void 관리자권한_부여() {
        //when
        joinService.joinProcess(joinInfo);

        //then
        User savedUser = userRepository.findByLoginId(joinInfo.getLoginId())
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        assertTrue(savedUser.getRoles().contains(UserRole.ADMIN));
        assertTrue(savedUser.getRoles().contains(UserRole.HOST));
        assertTrue(savedUser.getRoles().contains(UserRole.COMMUNITY_CREATOR));
    }

    @Test
    public void 채팅방개설권한_부여() {
        //when
        userRoleService.addCommunityCreatorRole(joinInfo.getLoginId());

        //then
        User savedUser = userRepository.findByLoginId(joinInfo.getLoginId())
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        assertTrue(savedUser.getRoles().contains(UserRole.COMMUNITY_CREATOR));
    }


    @Test
    public void 사용자_권한_부여() {

        String loginId = "xoghks211@naver.com";

        //when
        userRoleService.addAdminRole(loginId);

        User authUser = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 ㅇ벗읍니다."));
        assertTrue(authUser.getRoles().contains(UserRole.ADMIN));
        assertTrue(authUser.getRoles().contains(UserRole.HOST));
        assertTrue(authUser.getRoles().contains(UserRole.COMMUNITY_CREATOR));
        assertTrue(authUser.getRoles().contains(UserRole.USER));

    }
}
