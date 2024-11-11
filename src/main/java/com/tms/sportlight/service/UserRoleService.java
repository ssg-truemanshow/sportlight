package com.tms.sportlight.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private final UserRepository userRepository;

    public UserRoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 특정 사용자에게 지정된 역할을 추가. 사용자가 이미 해당 역할을 가지고 있는 경우에는 추가하지 않음
     *
     * @param loginId 역할을 추가할 사용자의 로그인 ID
     * @param role 추가할 역할
     * @throws IllegalArgumentException 사용자를 로그인 ID로 찾지 못한 경우 발생함
     */
    private void addRole(String loginId, UserRole role) {
        User user = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    /**
     * 사용자에게 HOST 역할을 부여하면서 채팅방 개설 자격도 추가
     *
     * @param loginId HOST 역할을 추가할 사용자의 로그인 ID
     */
    public void addHostRole(String loginId) {
        addRole(loginId, UserRole.HOST);
        addRole(loginId, UserRole.COMMUNITY_CREATOR);
    }

    /**
     * ADMIN 역할을 부여
     *
     * @param loginId ADMIN 역할을 추가할 사용자의 로그인 ID
     */
    public void addAdminRole(String loginId) {
        for (UserRole role : UserRole.values()) {
            addRole(loginId, role);
        }
    }

    /**
     * 사용자에게 COMMUNITY_CREATOR 역할을 추가
     *
     * @param loginId COMMUNITY_CREATOR 역할을 추가할 사용자의 로그인 ID
     */
    public void addCommunityCreatorRole(String loginId) {
        addRole(loginId, UserRole.COMMUNITY_CREATOR);
    }
}
