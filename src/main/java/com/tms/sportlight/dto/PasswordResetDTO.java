package com.tms.sportlight.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDTO {

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private String newPwd;

}
