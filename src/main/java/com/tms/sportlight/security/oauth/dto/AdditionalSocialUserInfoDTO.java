package com.tms.sportlight.security.oauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalSocialUserInfoDTO {

    private Long userId;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String loginId;

    @NotBlank(message = "이름은 필수입니다")
    private String userName;

    @NotBlank(message = "전화번호는 필수입니다")
    private String userPhone;

    private String userGender;

    //요기 유효성
    private String userBirth;

    @NotNull(message = "서비스 이용약관 동의는 필수입니다")
    private Boolean termsAgreement;

    private Boolean marketingAgreement;
    private Boolean personalAgreement;

}
