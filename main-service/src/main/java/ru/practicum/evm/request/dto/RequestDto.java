package ru.practicum.evm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ru.practicum.evm.request.enums.RequestState;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record RequestDto(
        @NotBlank
        LocalDateTime created,
        @NotBlank
        long event,
        @NotBlank
        long id,
        @NotBlank
        long requester,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        RequestState status) {

    @AssertTrue(message = "id must not be equal to requester")
    public boolean isIdNotEqualToRequester() {
        return id != requester;
    }
}
