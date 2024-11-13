package ru.practicum.evm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.practicum.evm.request.enums.RequestState;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record RequestResponseDto(
        LocalDateTime created,

        long event,

        long id,

        long requester,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        RequestState status
) {
}
