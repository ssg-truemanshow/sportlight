package com.tms.sportlight.dto;

import com.tms.sportlight.domain.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageSubDTO {

    @NotNull
    private ChatType type;
    @NotBlank
    private String message;

    public ChatMessage getChatMessage(int communityId, long userId, String userName) {
        if(type == null || message == null || userName == null) {
            throw new IllegalArgumentException();
        } else if(ChatType.ENTER.equals(type)) {
            message = userName + " 님이 입장하셨습니다.";
        }
        return ChatMessage.builder()
                .communityId(communityId)
                .userId(userId)
                .userName(userName)
                .message(message)
                .timestamp(LocalDateTime.now())
                .type(type)
                .build();
    }

}
