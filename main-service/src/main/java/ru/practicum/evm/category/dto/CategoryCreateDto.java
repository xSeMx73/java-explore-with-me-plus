package ru.practicum.evm.category.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record CategoryCreateDto(
        Long id,
        String name
) {
}
