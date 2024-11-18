package com.tms.sportlight.security;

import com.tms.sportlight.domain.User;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * User 엔티티의 권한 리스트를 GrantedAuthority 컬렉션으로 변환하여 반환
     *
     * @return GrantedAuthority 로 변환된 사용자 역할 컬렉션
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.name()))
            .collect(Collectors.toList());
    }

    /**
     * User 엔티티의 loginPwd 필드를 반환
     *
     * @return 로그인 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getLoginPwd();
    }

    /**
     * User 엔티티의 loginId 필드를 반환
     *
     * @return 로그인 아이디
     */
    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 아이디(해당 정보의 삭제 유/무)의 상태를 반환
     * User 엔티티의 isDeleted 필드가 false 일 경우 true 를 반환하여 활성화 상태임을 나타냄
     *
     * @return 계정이 활성화된 상태인지 여부
     */
    @Override
    public boolean isEnabled() {
        return !user.getIsDeleted();
    }
}
