package ru.practicum.evm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.practicum.evm.category.dto.CategoryDto;
import ru.practicum.evm.event.enums.EventState;
import ru.practicum.evm.event.model.Location;
import ru.practicum.evm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
record EventFullDto(

        Long id,
        UserShortDto initiator,
        String title,
        CategoryDto category,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,

        Location location,
        String annotation,
        String description,
        Long participantLimit,
        Boolean paid,
        Boolean requestModeration,
        Long confirmedRequests,
        Long views,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdOn,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime publishedOn,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        EventState state) {

}
