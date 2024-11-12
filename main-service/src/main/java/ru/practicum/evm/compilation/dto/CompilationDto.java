package ru.practicum.evm.compilation.dto;

import lombok.Builder;
import ru.practicum.evm.event.dto.EventShortResponseDto;

import java.util.Set;

@Builder
public record CompilationDto(
        Set<EventShortResponseDto> events,
        Long id,
        boolean pinned,
        String title) {
}
