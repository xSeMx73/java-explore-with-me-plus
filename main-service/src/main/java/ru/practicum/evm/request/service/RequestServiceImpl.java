package ru.practicum.evm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.evm.event.enums.EventState;
import ru.practicum.evm.event.model.Event;
import ru.practicum.evm.exception.NotFoundException;
import ru.practicum.evm.exception.NotPossibleException;
import ru.practicum.evm.request.dto.RequestDto;
import ru.practicum.evm.request.enums.RequestState;
import ru.practicum.evm.request.mapper.RequestMapper;
import ru.practicum.evm.request.model.Request;
import ru.practicum.evm.request.repository.RequestRepository;
import ru.practicum.evm.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl extends RequestService {
    private final RequestRepository repository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public RequestDto create(long userId, long eventId) {
        if (!repository.findAllByRequesterIdAndEventIdAndStatusNotLike(userId, eventId,
                RequestState.CANCELED).isEmpty())
            throw new NotPossibleException("Request already exists");
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);
        if (userId == event.getInitiator().getId())
            throw new NotPossibleException("User is Initiator of event");
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new NotPossibleException("Event is not published");
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit())
            throw new NotPossibleException("Request limit exceeded");
        Request newRequest = new Request();
        newRequest.setRequester(user);
        newRequest.setEvent(event);
        if (event.isRequestModeration() && event.getParticipantLimit() != 0) {
            newRequest.setStatus(RequestState.PENDING);
        } else {
            newRequest.setStatus(RequestState.CONFIRMED);
        }
        return RequestMapper.mapToRequestDto(repository.save(newRequest));
    }

    @Override
    public List<RequestDto> getAllRequestByUserId(final long userId) {
        userService.getById(userId);
        return repository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::mapToRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public RequestDto cancel(final long userId, final long requestId) {
        userService.getById(userId);
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Not found for request id" + requestId));
        if (!request.getRequester().getId().equals(userId))
            throw new NotPossibleException("Request is not by user");
        request.setStatus(RequestState.CANCELED);
        return RequestMapper.mapToRequestDto(repository.save(request));
    }
}
