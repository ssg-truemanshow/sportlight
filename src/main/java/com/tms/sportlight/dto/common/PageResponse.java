package com.tms.sportlight.dto.common;

import com.tms.sportlight.dto.PageRequestDTO;
import lombok.Getter;

import java.util.List;

/**
 * 요청 성공 시 반환하는 API Response Object
 * 목록 조회 요청에서 페이징 정보가 포함된 응답에 사용
 *
 * @param <T> data 타입
 */
@Getter
public class PageResponse<T> extends APIResponse {
    private final List<T> data;
    private final int page;
    private final int size;
    private final int total;
    private final int start; //시작 페이지 번호
    private final int end; //끝 페이지 번호
    private final boolean prev; //이전 페이지가 존재 하는지
    private final boolean next; //다음 페이지가 존재 하는지

    public PageResponse(PageRequestDTO pageRequestDTO, List<T> data, int total){
        super(200, "OK");
        this.data = data;
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        int temp = (int) (Math.ceil(this.page / 10.0)) * 10;
        this.start = temp - 9;
        int last =(int) (Math.ceil(total / (double) size));
        this.end = Math.min(temp, last);
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
