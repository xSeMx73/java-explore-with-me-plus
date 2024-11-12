package ru.practicum.evm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ru.practicum.evm.event.model.Location;
import ru.practicum.evm.event.UserAction;

import java.time.LocalDateTime;

record UpdateEventUserRequest(

        @NotBlank
        @Size(min = 3, max = 120)
        String title,

        Long category,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,

        Location location,

        @NotBlank
        @Size(min = 20, max = 2000)
        String annotation,

        @NotBlank
        @Size(min = 20, max = 7000)
        String description,

        @PositiveOrZero
        Long participantLimit,

        Boolean paid,
        Boolean requestModeration,
        UserAction stateAction
) {

}
