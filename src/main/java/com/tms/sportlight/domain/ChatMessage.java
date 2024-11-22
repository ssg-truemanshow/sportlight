package com.tms.sportlight.domain;

import com.tms.sportlight.dto.ChatType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "messages")
@Builder
public class ChatMessage {

    @Id
    private String id;
    private int communityId;
    private long userId;
    private String userName;
    private String message;
    private LocalDateTime timestamp;
    private ChatType type;

}
