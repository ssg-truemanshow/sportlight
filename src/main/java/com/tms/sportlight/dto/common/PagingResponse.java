package com.tms.sportlight.dto.common;

import com.tms.sportlight.dto.PageInfo;
import lombok.Getter;

import java.util.List;

/**
 * 요청 성공 시 반환하는 API Response Object
 * 목록 조회 요청에서 페이징 정보가 포함된 응답에 사용
 *
 * @param <T> data 타입
 */
@Getter
public class PagingResponse<T> extends APIResponse {
    private final List<T> data;
    private final PageInfo pageInfo;

    private PagingResponse(List<T> data, PageInfo pageInfo) {
        super(200, "OK");
        this.data = data;
        this.pageInfo = pageInfo;
    }

    public static <T> PagingResponse<T> of(List<T> data, PageInfo pageInfo) {
        return new PagingResponse<>(data, pageInfo);
    }
}
