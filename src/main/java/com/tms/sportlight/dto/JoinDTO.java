package com.tms.sportlight.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JoinDTO {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String loginId;

    private String loginPwd;

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

    private String socialLogin;

    private Boolean isDeleted;
}
