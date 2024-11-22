package com.tms.sportlight.service;

import com.tms.sportlight.domain.ChatMessage;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MongoTemplate mongoTemplate;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public void saveMessage(ChatMessage chatMessage) {
        mongoTemplate.save(chatMessage);
    }

    public List<ChatMessageDTO> getMessages(int communityId, String lastMessageId, int size, User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("communityId").is(communityId)
                .and("_id").lt(lastMessageId == null ? new ObjectId() : new ObjectId(lastMessageId)));
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(size);
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class);
        Collections.reverse(messages);
        return messages.stream()
                .map(message -> ChatMessageDTO.builder()
                        .type(message.getType())
                        .userId(message.getUserId())
                        .userName(message.getUserName())
                        .message(message.getMessage())
                        .time(TIME_FORMAT.format(message.getTimestamp()))
                        .isSender(user.getId() == message.getUserId())
                        .build())
                .toList();
    }
}
