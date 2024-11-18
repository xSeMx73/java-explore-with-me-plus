package ru.practicum.evm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.evm.category.CategoryRepository;
import ru.practicum.evm.category.model.Category;
import ru.practicum.evm.event.EventMapper;
import ru.practicum.evm.event.EventRepository;
import ru.practicum.evm.event.dto.*;
import ru.practicum.evm.event.enums.EventState;
import ru.practicum.evm.event.enums.StateAction;
import ru.practicum.evm.event.model.Event;
import ru.practicum.evm.exception.ConflictException;
import ru.practicum.evm.exception.NotFoundException;
import ru.practicum.evm.user.model.User;
import ru.practicum.evm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found";

    @Override
    public List<EventFullResponseDto> getEvents(Long userId, Integer from, Integer size) {
        getUser(userId);
        Pageable pageable = PageRequest.of(from, size);
        return repository.findByInitiatorId(userId, pageable).stream()
                .map(this::eventToDto)
                .toList();
    }

    @Override
    public EventFullResponseDto getEventById(Long userId, Long id, String ip, String uri) {
        getUser(userId);
        Optional<Event> event = repository.findByIdAndInitiatorId(id, userId);
        if (event.isEmpty()) {
            throw new NotFoundException(EVENT_NOT_FOUND_MESSAGE);
        }
        return eventToDto(event.get());
    }

    @Override
    public EventFullResponseDto createEvent(Long userId, NewEventDto eventDto) {
        User user = getUser(userId);
        Category category = getCategory(eventDto.category());
        Event event = EventMapper.mapCreateDtoToEvent(eventDto);
        if (event.getPaid() == null) {
            event.setPaid(false);
        }

        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        event.setInitiator(user);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        Event newEvent = repository.save(event);
        return eventToDto(newEvent);
    }

    @Override
    public EventFullResponseDto updateEvent(Long userId, UpdateEventUserRequest eventDto, Long eventId) {
        getUser(userId);
        Optional<Event> eventOptional = repository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException(EVENT_NOT_FOUND_MESSAGE);
        }
        Event foundEvent = eventOptional.get();
        if (foundEvent.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя изменять изменять сообщение, которое опубликовано");
        }
        updateEventFields(eventDto, foundEvent);
        Event saved = repository.save(foundEvent);
        return eventToDto(saved);
    }

    @Override
    public List<EventFullResponseDto> publicGetEvents(PublicGetEventRequestDto requestParams,
                                                      HttpServletRequest request) {
        LocalDateTime start = (requestParams.rangeStart() == null) ?
                LocalDateTime.now() : requestParams.rangeStart();
        LocalDateTime end = (requestParams.rangeEnd() == null) ?
                LocalDateTime.now().plusYears(10) : requestParams.rangeEnd();

        if (start.isAfter(end))
            throw new ValidationException("Дата окончания, должна быть больше даты старта.");
        List<Event> events = repository.findEventsPublic(
                requestParams.text(),
                requestParams.categories(),
                requestParams.paid(),
                start,
                end,
                EventState.PUBLISHED,
                requestParams.onlyAvailable(),
                PageRequest.of(requestParams.from() / requestParams.size(),
                        requestParams.size())
        );

        if (!events.isEmpty()) {
            events = repository.saveAll(events);
        }

        return EventMapper.mapToEventDto(events);
    }

    @Override
    public EventFullResponseDto publicGetEvent(Long id, HttpServletRequest request) {
        Event event = getEvent(id);

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие не найдено");
        }

        event = repository.save(event);
        return EventMapper.mapEventToEventDto(event);
    }

    @Override
    public List<EventFullResponseDto> adminGetEvents(AdminGetEventRequestDto requestParams) {
        List<Event> events = repository.findEventsByAdmin(
                requestParams.users(),
                requestParams.states(),
                requestParams.categories(),
                requestParams.rangeStart(),
                requestParams.rangeEnd(),
                PageRequest.of(requestParams.from() / requestParams.size(),
                        requestParams.size())
        );
        return EventMapper.mapToEventDto(events);
    }

    @Override
    public EventFullResponseDto adminChangeEvent(Long eventId, UpdateEventUserRequest eventDto) {
        Event event = getEvent(eventId);
        checkEventForUpdate(event, eventDto.stateAction());
        Event updatedEvent = repository.save(prepareEventForUpdate(event, eventDto));
        return EventMapper.mapEventToEventDto(updatedEvent);
    }

    private EventFullResponseDto eventToDto(Event event) {
        return EventMapper.mapEventToEventDto(event);
    }

    private Event getEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_MESSAGE));
    }

    private void checkEventForUpdate(Event event, StateAction action) {
        checkEventDate(event.getEventDate());
        if (action == null) return;
        if (action.equals(StateAction.PUBLISH_EVENT)
                && !event.getState().equals(EventState.PENDING))
            throw new ConflictException("Опубликовать событие можно в статусе PENDING, а статус = "
                    + event.getState());
        if (action.equals(StateAction.REJECT_EVENT)
                && event.getState().equals(EventState.PUBLISHED))
            throw new ConflictException("Отменить событие можно только в статусе PUBLISHED, а статус = "
                    + event.getState());
    }

    private Event prepareEventForUpdate(Event event, UpdateEventUserRequest updateEventDto) {
        if (updateEventDto.annotation() != null)
            event.setAnnotation(updateEventDto.annotation());
        if (updateEventDto.description() != null)
            event.setDescription(updateEventDto.description());
        if (updateEventDto.eventDate() != null) {
            checkEventDate(updateEventDto.eventDate());
            event.setEventDate(updateEventDto.eventDate());
        }
        if (updateEventDto.paid() != null)
            event.setPaid(updateEventDto.paid());
        if (updateEventDto.participantLimit() != null)
            event.setParticipantLimit(updateEventDto.participantLimit());
        if (updateEventDto.title() != null)
            event.setTitle(updateEventDto.title());
        if (updateEventDto.stateAction() != null) {
            switch (updateEventDto.stateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        return event;
    }

    private void checkEventDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now().plusHours(1)))
            throw new ConflictException("Дата начала события меньше чем час " + dateTime);
    }

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user.get();
    }

    private Category getCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException("Категория не найдена");
        }
        return category.get();
    }


    private void updateEventFields(UpdateEventUserRequest eventDto, Event foundEvent) {
        if (eventDto.category() != null) {
            Category category = getCategory(eventDto.category());
            foundEvent.setCategory(category);
        }

        if (eventDto.annotation() != null && !eventDto.annotation().isBlank()) {
            foundEvent.setAnnotation(eventDto.annotation());
        }
        if (eventDto.description() != null && !eventDto.description().isBlank()) {
            foundEvent.setDescription(eventDto.description());
        }
        if (eventDto.eventDate() != null) {
            if (eventDto.eventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Дата начала события не может быть раньше чем через 2 часа");
            }
            foundEvent.setEventDate(eventDto.eventDate());
        }
        if (eventDto.paid() != null) {
            foundEvent.setPaid(eventDto.paid());
        }
        if (eventDto.participantLimit() != null) {
            if (eventDto.participantLimit() < 0) {
                throw new ValidationException("Participant limit cannot be negative");
            }
            foundEvent.setParticipantLimit(eventDto.participantLimit());
        }
        if (eventDto.requestModeration() != null) {
            foundEvent.setRequestModeration(eventDto.requestModeration());
        }
        if (eventDto.title() != null && !eventDto.title().isBlank()) {
            foundEvent.setTitle(eventDto.title());
        }
        if (eventDto.location() != null) {
            if (eventDto.location().getLat() != null) {
                foundEvent.getLocation().setLat(eventDto.location().getLat());
            }
            if (eventDto.location().getLon() != null) {
                foundEvent.getLocation().setLon(eventDto.location().getLon());
            }
        }

        if (eventDto.stateAction() != null) {
            switch (eventDto.stateAction()) {
                case CANCEL_REVIEW -> foundEvent.setState(EventState.CANCELED);
                case PUBLISH_EVENT -> foundEvent.setState(EventState.PUBLISHED);
                case SEND_TO_REVIEW -> foundEvent.setState(EventState.PENDING);
            }
        }
    }


    private List<String> getListOfUri(List<Event> events, String uri) {
        return events.stream().map(Event::getId).map(id -> getUriForEvent(uri, id))
                .collect(Collectors.toList());
    }

    private String getUriForEvent(String uri, Long eventId) {
        return uri + "/" + eventId;
    }
}
