package com.tms.sportlight.controller;

import com.tms.sportlight.dto.EventDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public DataResponse<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        EventDTO savedEvent = eventService.saveEvent(eventDTO);
        return DataResponse.of(savedEvent);
    }

    @GetMapping("/{id}")
    public DataResponse<EventDTO> getEventById(@PathVariable int id) {
        EventDTO event = eventService.getEventById(id);
        return DataResponse.of(event);
    }

    @PatchMapping("/{id}")
    public DataResponse<Void> updateEvent(@PathVariable int id, @RequestBody EventDTO eventDTO) {
        eventService.updateEvent(id, eventDTO);
        return DataResponse.empty();
    }

    @DeleteMapping("/{id}")
    public DataResponse<Void> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return DataResponse.empty();
    }
}
