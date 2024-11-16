package ru.practicum.evm.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record CategoryCreateDto(
        Long id,

        @NotBlank
        String name
) {
}
