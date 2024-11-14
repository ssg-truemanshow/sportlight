package com.tms.sportlight.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class UserRoleConverter implements AttributeConverter<List<UserRole>, String> {

    private static final String SEPARATOR = ",";

    /**
     * List<UserRole>을 데이터베이스에 저장하기 위해 하나의 문자열로 변환
     *
     * @param userRoles 변환할 UserRole 리스트. null 이면 예외가 발생
     * @return 구분자로 연결된 문자열로 변환된 UserRole 리스트
     * @throws IllegalArgumentException userRoles 가 null 일 경우 예외를 발생
     */
    @Override
    public String convertToDatabaseColumn(List<UserRole> userRoles) {

        if(userRoles == null){
            throw new IllegalArgumentException("null 이면 안됨");
        }

        return userRoles.stream()
            .map(UserRole::name)
            .collect(Collectors.joining(SEPARATOR));
    }


    /**
     * 데이터베이스에서 불러온 문자열을 List<UserRole>로 변환
     *
     * @param s 변환할 문자열. null 이면 예외가 발생
     * @return 문자열을 분리하여 생성된 UserRole 리스트
     * @throws IllegalArgumentException 문자열 s가 null 일 경우 예외를 발생시킵니다.
     */
    @Override
    public List<UserRole> convertToEntityAttribute(String s) {

        if (s == null) {
            throw new IllegalArgumentException("null 안댐");
        }

        return new ArrayList<>(Arrays.stream(s.split(SEPARATOR))
            .map(UserRole::valueOf)
            .collect(Collectors.toList()));
    }

}
