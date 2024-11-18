package com.tms.sportlight.service;

import com.tms.sportlight.domain.Event;
import com.tms.sportlight.dto.EventDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public EventDTO saveEvent(EventDTO eventDTO) {
        Event event = Event.builder()
                .name(eventDTO.getName())
                .content(eventDTO.getContent())
                .startDate(eventDTO.getStartDate())
                .endDate(eventDTO.getEndDate())
                .classLink(eventDTO.getClassLink())
                .regDate(LocalDateTime.now())
                .num(eventDTO.getNum())
                .status(1)  // 기본 상태 설정
                .build();

        Event savedEvent = eventRepository.save(event);
        return EventDTO.fromEntity(savedEvent);
    }

    @Transactional
    public void updateEvent(int id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE, "해당 ID로 이벤트를 찾을 수 없습니다: " + id));
        event.updateEvent(eventDTO.getName(), eventDTO.getClassLink(), eventDTO.getNum());
    }

    @Transactional(readOnly = true)
    public EventDTO getEventById(int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE, "해당 ID로 이벤트를 찾을 수 없습니다: " + id));
        return EventDTO.fromEntity(event);
    }

    @Transactional
    public void deleteEvent(int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE, "해당 ID로 이벤트를 찾을 수 없습니다: " + id));
        eventRepository.delete(event);
    }
}
