package com.tms.sportlight.security.auth;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class VerificationCode {
    private final String code;
    private final LocalDateTime expirationTime;

    public VerificationCode(String code, int minutesToExpire) {
        this.code = code;
        this.expirationTime = LocalDateTime.now().plusMinutes(minutesToExpire);
    }

    public boolean isValid(String inputCode) {
        return this.code.equals(inputCode) && LocalDateTime.now().isBefore(expirationTime);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }
}
