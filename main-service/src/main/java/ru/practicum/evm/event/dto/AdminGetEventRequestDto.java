package ru.practicum.evm.event.dto;

import lombok.Builder;
import ru.practicum.evm.event.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record AdminGetEventRequestDto(
        List<Long> users,
        List<EventState> states,
        List<Long> categories,
        LocalDateTime rangeStart,
        LocalDateTime rangeEnd,
        int from,
        int size
) {


}
