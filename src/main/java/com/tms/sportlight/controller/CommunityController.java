package com.tms.sportlight.controller;

import com.tms.sportlight.domain.ChatMessage;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.dto.common.PageRequestDTO;
import com.tms.sportlight.dto.common.PageResponse;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.ChatMessageService;
import com.tms.sportlight.service.CommunityService;
import com.tms.sportlight.service.RedisMessagePublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final ChatMessageService chatMessageService;
    private final RedisMessagePublisher redisMessagePublisher;

    @PostMapping("/communities")
    public DataResponse<Id> createCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @Valid CommunityCreateDTO createDTO) {
        User user = userDetails.getUser();
        int id = communityService.save(user, createDTO);
        return DataResponse.of(new Id(id));
    }

    @PatchMapping("/communities/{id}")
    public DataResponse<Void> modifyCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Id id,
                                              @Valid CommunityUpdateDTO updateDTO) {
        communityService.updateCommunity(id.getId(), userDetails.getUser(), updateDTO);
        return DataResponse.empty();
    }

    @GetMapping("/communities")
    public PageResponse<CommunityListDTO> getList(@Valid PageRequestDTO<CommunitySearchDTO> pageRequestDTO) {
        List<CommunityListDTO> dtoList = communityService.getCommunityList(pageRequestDTO);
        int total = communityService.getCommunityCount(pageRequestDTO);
        return new PageResponse<>(pageRequestDTO, dtoList, total);
    }

    /*@GetMapping("/communities/{id}")
    public DataResponse<CommunityDetailDTO> get(@PathVariable Id id) {
        CommunityDetailDTO communityDetail = communityService.getCommunityDetail(id.getId());
        return DataResponse.of(communityDetail);
    }*/

    @GetMapping("/communities/{id}/messages")
    public List<ChatMessageDTO> getMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable Id id,
                                           @RequestParam(required = false) String lastMessageId,
                                           @RequestParam(defaultValue = "150") Integer size) {
        User user = customUserDetails.getUser();
        if(!communityService.isCommunityParticipant(user, id.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return chatMessageService.getMessages(id.getId(), lastMessageId, size, user);
    }

    @DeleteMapping("/communities/{id}")
    public DataResponse<Void> deleteCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Id id) {
        communityService.deleteCommunity(id.getId(), userDetails.getUser());
        return DataResponse.empty();
    }

    // /pub/community/{communityId}
    @MessageMapping("/community/{communityId}")
    public String sendMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @DestinationVariable Id communityId,
                              @RequestBody MessageSubDTO messageSubDTO) {
        User user = customUserDetails.getUser();
        ChatMessage chatMessage = messageSubDTO.getChatMessage(communityId.getId(), user.getId(), user.getUserName());
        redisMessagePublisher.publish(chatMessage);
        chatMessageService.saveMessage(chatMessage);
        return "ok";
    }

    @PostMapping("/communities/{id}/enter")
    public DataResponse<Void> enter(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable Id id) {
        communityService.enterCommunity(id.getId(), customUserDetails.getUser());
        return DataResponse.empty();
    }

    @PostMapping("/communities/{id}/exit")
    public DataResponse<Void> exit(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable Id id) {
        communityService.exitCommunity(id.getId(), customUserDetails.getUser());
        return DataResponse.empty();
    }

    @PostMapping("/communities/{communityId}/kick/{userId}")
    public DataResponse<Void> kick(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @PathVariable Id communityId,
                                   @PathVariable Id userId) {
        communityService.kickParticipant(communityId.getId(), userId.getId(), customUserDetails.getUser());
        return DataResponse.empty();
    }
}
