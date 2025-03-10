package com.tms.sportlight.controller.formatter;

import com.tms.sportlight.dto.Id;
import org.springframework.core.convert.converter.Converter;

/**
 * HTTP 요청 파라미터를 Id 타입으로 변환하는 컨버터 클래스
 * Converter 인터페이스 구현
 */
public class IdConverter implements Converter<String, Id> {

    /**
     * String 타입을 Id 타입으로 변환
     *
     * @param source 변환할 String 객체
     * @return 변환된 Id 타입 객체
     */
    @Override
    public Id convert(String source) {
        return Id.from(source);
    }

}
