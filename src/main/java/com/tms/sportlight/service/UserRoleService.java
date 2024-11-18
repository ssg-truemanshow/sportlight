package com.tms.sportlight.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRoleService {

    private final UserRepository userRepository;

    public UserRoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(String loginId){

        return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * 사용자에게 HOST 역할을 부여하면서 채팅방 개설 권한도 추가
     *
     * @param loginId HOST 역할을 추가할 사용자의 로그인 ID
     */
    public void addHostRole(String loginId) {

        User saveUser = findUser(loginId);

        saveUser.addRole(UserRole.HOST);
        saveUser.addRole(UserRole.COMMUNITY_CREATOR);
    }

    /**
     * ADMIN 역할을 부여
     *
     * @param loginId ADMIN 역할을 추가할 사용자의 로그인 ID
     */
    public void addAdminRole(String loginId) {

        User saveUser = findUser(loginId);

        for (UserRole role : UserRole.values()) {
            saveUser.addRole(role);
        }
    }

    /**
     * 사용자에게 COMMUNITY_CREATOR 역할을 추가
     *
     * @param loginId COMMUNITY_CREATOR 역할을 추가할 사용자의 로그인 ID
     */
    public void addCommunityCreatorRole(String loginId) {

        User saveUser = findUser(loginId);

        saveUser.addRole(UserRole.COMMUNITY_CREATOR);
    }
}
