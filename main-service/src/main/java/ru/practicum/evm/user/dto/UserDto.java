package ru.practicum.evm.user.dto;

import lombok.Builder;

@Builder
public record UserDto(
        long id,
        String email,
        String name) {
}
