package ru.practicum.evm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evm.event.dto.EventFullResponseDto;
import ru.practicum.evm.event.dto.PublicGetEventRequestDto;
import ru.practicum.evm.event.service.EventService;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullResponseDto> publicGetEvents(HttpServletRequest request, PublicGetEventRequestDto requestParams) {
        log.info("Получить события, согласно устловиям -> {}", requestParams);
        return eventService.publicGetEvents(requestParams, request);
    }

    @GetMapping("/{id}")
    EventFullResponseDto publicGetEvent(@PathVariable Long id,
                            HttpServletRequest request) {
        return eventService.publicGetEvent(id, request);
    }
}