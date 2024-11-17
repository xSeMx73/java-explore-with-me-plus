package ru.practicum.evm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record NewCompilationDto(
        Set<Long> events,
        @NotBlank
        boolean pinned,
        @NotBlank
        @Size(min = 1, max = 50)
        String title) {
}
