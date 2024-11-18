package com.tms.sportlight.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDTO {

    private String token;

    private String newPwd;

}
