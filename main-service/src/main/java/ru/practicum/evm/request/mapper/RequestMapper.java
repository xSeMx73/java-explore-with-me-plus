package ru.practicum.evm.request.mapper;

import ru.practicum.evm.request.dto.RequestDto;
import ru.practicum.evm.request.model.Request;

import java.util.List;

public class RequestMapper {
    private RequestMapper() {
    }

    public static RequestDto mapToRequestDto(Request request) {
        return RequestDto.builder()
                .requester(request.getRequester().getId())
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<RequestDto> mapToRequestDto(final List<Request> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream()
                .map(RequestMapper::mapToRequestDto)
                .toList();
    }
}
