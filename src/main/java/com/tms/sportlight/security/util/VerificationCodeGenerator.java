package com.tms.sportlight.security.util;

import java.security.SecureRandom;

public class VerificationCodeGenerator {

    final static SecureRandom verificationCode = new SecureRandom();
    private static final int CODE_LENGTH = 6;

    public static String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {

            code.append(verificationCode.nextInt(10));

        }
        return code.toString();
    }

}
