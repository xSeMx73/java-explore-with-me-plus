package ru.practicum.evm.event.dto;

import jakarta.validation.constraints.NotNull;
import ru.practicum.evm.request.enums.RequestState;

import java.util.List;

public record UpdateEventRequestStatusDto(

        List<Long> requestIds,

        @NotNull
        RequestState status
) {

}
