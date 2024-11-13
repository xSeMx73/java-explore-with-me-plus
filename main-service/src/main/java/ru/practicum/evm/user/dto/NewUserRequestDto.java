package ru.practicum.evm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserRequestDto(
        @NotBlank
        @Email
        @Size(min = 6, max = 64)
        String email,

        @NotBlank
        @Size(min = 2, max = 32)
        String name) {
}
