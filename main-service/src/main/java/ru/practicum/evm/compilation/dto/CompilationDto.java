package ru.practicum.evm.compilation.dto;

import lombok.Builder;
import ru.practicum.evm.event.dto.EventShortDto;

import java.util.Set;

@Builder
public record CompilationDto(
        Set<EventShortDto> events,
        Long id,
        boolean pinned,
        String title) {
}
