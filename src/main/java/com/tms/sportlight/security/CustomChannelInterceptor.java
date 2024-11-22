package com.tms.sportlight.security;

import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final CommunityService communityService;

    /**
     * 연결할 때: 토큰 유무
     * 구독할 때: 토큰 유무, 회원 여부, 채팅방 존재 여부
     * 연결끊을 때: 토큰 유무, 회원 여부, 채팅방 참가 여부
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if(sessionAttributes == null) {
                throw new IllegalArgumentException("session attributes is null");
            }
            Object auth = sessionAttributes.get("auth");
            if(!(auth instanceof UsernamePasswordAuthenticationToken)) {
                throw new BizException(ErrorCode.AUTHENTICATION_ERROR);
            }
            String username = (String) ((Authentication) auth).getPrincipal();
            String simpDestination = (String) message.getHeaders().get("simpDestination");
            log.error("simpDestination={}", simpDestination);
            int communityId = parseCommunityId(simpDestination);
            if(!communityService.isCommunityParticipant(username, communityId)) {
                log.error("채팅 참가자가 아닙니다.");
                throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
            }
            accessor.setUser((Authentication) auth);
        }
        return message;
    }

    private int parseCommunityId(String destination) {
        String SUB_PATH_PREFIX = "/sub/community/";
        if(destination == null || !destination.startsWith(SUB_PATH_PREFIX)) {
            throw new IllegalArgumentException("Destination does not start with " + SUB_PATH_PREFIX);
        }
        try {
            return Integer.parseInt(destination.substring(SUB_PATH_PREFIX.length()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
