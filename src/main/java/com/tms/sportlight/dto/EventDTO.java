package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer id;
    private String name;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String classLink;
    private LocalDateTime regDate;
    private Integer num;
    private int status;
    private List<CouponDTO> coupons;
    public static EventDTO fromEntity(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .content(event.getContent())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .classLink(event.getClassLink())
                .regDate(event.getRegDate())
                .num(event.getNum())
                .status(event.getStatus())
                .coupons(event.getCoupons().stream().map(CouponDTO::fromEntity).toList())
                .build();
    }

}
