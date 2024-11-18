package com.tms.sportlight.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
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
    private Boolean isDeleted;
    private String joinMethod;

    public void addRole(UserRole role) {
        if (roles == null) {
            roles = new ArrayList<>();
        } else if (roles.contains(role)) {
            return;
        } else {
            roles = new ArrayList<>(roles);
        }
        roles.add(role);
    }

    @PreUpdate
    public void userModTime() {
        this.modDate = LocalDateTime.now();
    }

    public void updatePassword(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public void update(String userNickname, String userIntroduce,Boolean marketingAgreement, Boolean personalAgreement) {
        if (userNickname != null) {
            this.userNickname = userNickname;
        }
        if (userIntroduce != null) {
            this.userIntroduce = userIntroduce;
        }
        if (marketingAgreement != null) {
            this.marketingAgreement = marketingAgreement;
        }
        if (personalAgreement != null) {
            this.personalAgreement = personalAgreement;
        }
    }


}
