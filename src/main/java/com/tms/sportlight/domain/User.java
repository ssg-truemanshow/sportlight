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
    private List<UserRole> roles = new ArrayList<>();

    private String userNickname;

    private String userIntroduce;

    private String userName;

    private String userGender;

    private String userBirth;

    private String userPhone;

    private double userLatitude;

    private double userLongitude;

    private Boolean termsAgreement;

    private Boolean marketingAgreement;

    private Boolean personalAgreement;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private LocalDateTime delDate;

    @Column(name = "user_state")
    private Boolean isDeleted;

    @Column(unique = true)
    private String socialLogin;
}
