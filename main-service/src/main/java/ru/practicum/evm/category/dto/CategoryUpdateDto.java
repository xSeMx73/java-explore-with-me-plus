package ru.practicum.evm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record CategoryUpdateDto(

        @NotBlank
        @Size(max = 50)
        String name) {

}
