package ru.practicum.evm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.practicum.evm.category.dto.CategoryDto;
import ru.practicum.evm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record EventShortDto(

        Long id,
        UserShortDto initiator,
        String title,
        CategoryDto category,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,

        String annotation,
        Boolean paid,
        Long confirmedRequests,
        Long views
) {
}
