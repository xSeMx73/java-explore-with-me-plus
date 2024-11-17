package ru.practicum.evm.compilation.dto;

import lombok.Builder;
import ru.practicum.evm.event.dto.EventShortResponseDto;

import java.util.Collection;


@Builder
public record CompilationDto(
       Collection<EventShortResponseDto> events,
        Long id,
        boolean pinned,
        String title) {
}
