package com.tms.sportlight.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JoinDTO {

    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String loginId;

    @NotBlank
    private String loginPwd;

    @NotBlank
    private String userNickname;

    private String userName;
    private String userGender;
    private String userBirth;
    private String userPhone;

    @NotNull
    private Boolean termsAgreement;

    private Boolean marketingAgreement;
    private Boolean personalAgreement;
    private String joinMethod;

    @NotNull
    private Boolean isDeleted;


}
