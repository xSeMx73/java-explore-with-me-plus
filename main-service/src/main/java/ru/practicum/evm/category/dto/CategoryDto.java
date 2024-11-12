package ru.practicum.evm.category.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record CategoryDto(

        Long id,
        String name) {

}
