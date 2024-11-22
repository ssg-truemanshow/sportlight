package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessageDTO {

    private ChatType type;
    private long userId;
    private String userName;
    private String message;
    private String time;
    private boolean isSender;

}
