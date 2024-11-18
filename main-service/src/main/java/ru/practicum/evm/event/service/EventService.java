package ru.practicum.evm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.evm.event.dto.*;

import java.util.List;

public interface EventService {
    List<EventFullResponseDto> getEvents(Long userId, Integer from, Integer size);

    EventFullResponseDto getEventById(Long userId, Long eventId, String ip, String uri);

    EventFullResponseDto createEvent(Long userId, NewEventDto eventDto);

    List<EventFullResponseDto> adminGetEvents(AdminGetEventRequestDto requestParams);

    EventFullResponseDto adminChangeEvent(Long eventId, UpdateEventUserRequest eventDto);

    EventFullResponseDto updateEvent(Long userId, UpdateEventUserRequest eventDto, Long eventId);

    List<EventFullResponseDto> publicGetEvents(PublicGetEventRequestDto requestParams, HttpServletRequest request);

    EventFullResponseDto publicGetEvent(Long eventId, HttpServletRequest request);
}
