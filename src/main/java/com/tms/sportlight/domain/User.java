package com.tms.sportlight.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String loginPwd;

    @Convert(converter = UserRoleConverter.class)
    @Builder.Default
    private List<UserRole> roles = new ArrayList<>();

    private String userNickname;

    private String userIntroduce;

    private String userName;

    private String userGender;

    private String userBirth;

    private String userPhone;

    private Boolean termsAgreement;

    private Boolean marketingAgreement;

    private Boolean personalAgreement;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private LocalDateTime delDate;

    @Column(name = "user_state")
    private Boolean isDeleted;

    @Column(name = "social_login")
    private String joinMethod;

    public void addRole(UserRole role) {
        if (roles == null) {
            roles = new ArrayList<>();
        } else if (roles.contains(role)) {
            return; // 이미 존재하는 역할이므로 추가하지 않음
        }
        roles.add(role);
    }


}
