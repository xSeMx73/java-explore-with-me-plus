package ru.practicum.evm.event;

import org.mapstruct.Mapper;
import ru.practicum.evm.event.dto.EventFullResponseDto;
import ru.practicum.evm.event.dto.NewEventDto;
import ru.practicum.evm.event.dto.UpdateEventUserRequest;
import ru.practicum.evm.event.model.Event;
import ru.practicum.evm.event.model.Location;
import ru.practicum.evm.category.CategoryMapper;
import ru.practicum.evm.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public class EventMapper {
    static CategoryMapper categoryMapper;
    static UserMapper userMapper;

    public static Event mapCreateDtoToEvent(NewEventDto dto) {
        Event event = new Event();
        event.setAnnotation(dto.annotation());
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(dto.description());
        event.setEventDate(dto.eventDate());
        event.setPaid(dto.paid());
        event.setParticipantLimit(dto.participantLimit());
        event.setRequestModeration(dto.requestModeration());
        event.setTitle(dto.title());
        event.getLocation().setLat(dto.location().getLat());
        event.getLocation().setLon(dto.location().getLon());
        return event;
    }

    public static EventFullResponseDto mapEventToEventDto(Event event) {
        Location location = event.getLocation();
        location.setLat(location.getLat());
        location.setLon(location.getLon());
        return EventFullResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .annotation(event.getAnnotation())
                .paid(event.getPaid())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .state(event.getState())
                .participantLimit(event.getParticipantLimit())
                .location(location)
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .requestModeration(event.getRequestModeration())
                .views((event.getViews() == null) ? 0L : event.getViews())
                .build();
    }

    public static List<EventFullResponseDto> mapToEventDto(Iterable<Event> events) {
        List<EventFullResponseDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(mapEventToEventDto(event));
        }
        return dtos;
    }

    public static Event mapUpdateDtoToEvent(UpdateEventUserRequest dto) {
        Event event = new Event();
        event.setAnnotation(dto.annotation());
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(dto.description());
        LocalDateTime dateTime = dto.eventDate();
        event.setEventDate(dateTime);
        event.setPaid(dto.paid());
        event.setParticipantLimit(dto.participantLimit());
        event.setRequestModeration(dto.requestModeration());
        event.setTitle(dto.title());
        if (dto.location() != null) {
            event.getLocation().setLat(dto.location().getLat());
            event.getLocation().setLon(dto.location().getLon());
        }
        return event;
    }
}
