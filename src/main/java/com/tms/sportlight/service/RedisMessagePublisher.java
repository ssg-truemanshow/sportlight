package com.tms.sportlight.service;

import com.tms.sportlight.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessagePublisher {

    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * channelTopic 을 구독하는 모든 구독자에게 메시지를 발행(publish)
     *
     * @param message 발행할 메시지
     */
    public void publish(ChatMessage message) {
        log.info("발행할 topic: {}", channelTopic.getTopic());
        redisTemplate.convertAndSend(channelTopic.getTopic() + "/" + message.getCommunityId(), message);
    }
}
