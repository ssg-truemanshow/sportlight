package com.tms.sportlight.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.sportlight.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisMessageListener implements MessageListener {

    private static final String SUB_PATH_PREFIX = "/sub/community/";
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessageListener(ObjectMapper objectMapper,
                                SimpMessageSendingOperations messagingTemplate,
                                @Qualifier("jsonObjectRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis 메시지를 ChatMessage 객체로 변환
            String messageContent = redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(messageContent, ChatMessage.class);
            // WebSocket을 통해 채팅방에 메시지 전송
            messagingTemplate.convertAndSend(SUB_PATH_PREFIX + chatMessage.getCommunityId(), chatMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
