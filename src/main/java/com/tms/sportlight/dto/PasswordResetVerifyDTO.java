package com.tms.sportlight.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetVerifyDTO {

    @NotBlank(message = "이름은 필수 값입니다.")
    private String userName;

    @NotBlank(message = "전화번호는 필수 값입니다.")
    private String userPhone;

    @NotBlank(message = "이메일은 필수 값입니다.")
    private String loginId;

    @NotBlank(message = "유효한 인증번호를 입력해주세요.")
    private String verificationCode;
}
