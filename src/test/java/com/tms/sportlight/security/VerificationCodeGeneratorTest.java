package com.tms.sportlight.security;

import com.tms.sportlight.security.util.VerificationCodeGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VerificationCodeGeneratorTest {

    @Test
    void testGenerateCode() {
        String code = VerificationCodeGenerator.generateCode();
        assertEquals(6, code.length(), "code.length 는 " + code.length() + "입니다.");
        System.out.println("Generated Code: " + code); // 생성된 코드 출력
    }
}
