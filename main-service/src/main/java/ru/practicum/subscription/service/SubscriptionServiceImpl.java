package ru.practicum.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.dto.converter.EventToEventShortResponseDtoConverter;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.ConditionsNotMetException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.mapper.SubscriptionMapper;
import ru.practicum.subscription.model.BlackList;
import ru.practicum.subscription.model.Subscriber;
import ru.practicum.subscription.repository.BlackListRepository;
import ru.practicum.subscription.repository.SubscriberRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriberRepository subscriberRepository;
    private final BlackListRepository blackListRepository;
    private final UserRepository userRepository;
    private final EventService eventService;
    private final EventToEventShortResponseDtoConverter listConverter;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public void addSubscriber(Subscriber subscriber) {
        log.debug("Проверка пользователя на существование в БД {}", subscriber.getUserId());
        User userSibscriber = getUser(subscriber.getUserId(), subscriber.getSubscriber());
        if (subscriberRepository
                .findByUserIdAndSubscriber(subscriber.getUserId(), subscriber.getSubscriber())
                .isPresent()) {
            throw new ConditionsNotMetException("Пользователь уже в черном списке на данного человека");
        }
        if (blackListRepository
                .findByUserIdAndBlockUser(subscriber.getUserId(), subscriber.getSubscriber())
                .isPresent()) {
            throw new ConditionsNotMetException("Пользователь находиться в черном списке и не может подписаться");
        }
        log.info("Сохранение пользователя в подписчиках {} {}", userSibscriber.getName(), userSibscriber.getEmail());
        subscriberRepository.save(subscriber);
    }

    @Override
    public void addBlacklist(BlackList blackList) {
        log.debug("Проверка пользователей на существование в БД {}", blackList.getUserId());
        User blockUser = getUser(blackList.getUserId(), blackList.getBlackList());
        if (blackListRepository
                .findByUserIdAndBlockUser(blackList.getUserId(), blackList.getBlackList())
                .isPresent()) {
            throw new ConditionsNotMetException("Пользователь уже в черном списке на данного человека");
        }
        if (subscriberRepository
                .findByUserIdAndSubscriber(blackList.getUserId(), blackList.getBlackList())
                .isPresent()) {
            throw new ConditionsNotMetException("Пользователь находиться в подписчиках и не может быть добавлен в черный список");
        }
        log.info("Сохранение пользователя в черный список {} {}", blockUser.getName(), blockUser.getEmail());
        blackListRepository.save(blackList);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        log.debug("Проверка пользователя на существование в БД {}", subscriber.getUserId());
        getUser(subscriber.getUserId(), subscriber.getSubscriber());
        Optional<Long> subscribed = subscriberRepository.findAllByUserId(subscriber.getUserId()).stream()
                .map(Subscriber::getSubscriber)
                .filter(s -> s.equals(subscriber.getSubscriber()))
                .findFirst();

        if (subscribed.isPresent()) {
            subscriberRepository.delete(subscriber);
        } else {
            throw new NotFoundException("Пользователя нет в подписчиках");
        }
    }

    @Override
    public void removeBlacklist(BlackList blackList) {
        log.debug("Проверка пользователей на существование в БД {}", blackList.getUserId());
        getUser(blackList.getUserId(), blackList.getBlackList());
        Optional<Long> blackLists = blackListRepository.findAllByUserId(blackList.getUserId()).stream()
                .map(BlackList::getBlackList)
                .filter(s -> s.equals(blackList.getBlackList()))
                .findFirst();

        if (blackLists.isPresent()) {
            blackListRepository.delete(blackList);
        } else {
            throw new NotFoundException("Пользователя нет в черном листе");
        }
    }

    @Override
    public SubscriptionDto getSubscribers(long userId) {
        List<Subscriber> subscriptions = subscriberRepository.findAllByUserId(userId);
        System.out.println(subscriptions.getFirst());
        return subscriptionMapper.SubscribertoSubscriptionDto(subscriptions);
    }

    @Override
    public SubscriptionDto getBlacklists(long userId) {
        Collection<BlackList> blackList = blackListRepository.findAllByUserId(userId);
        return subscriptionMapper.BlackListSubscriptionDto(blackList);
    }

    @Override
    public List<EventShortResponseDto> getEvents(long userId) {
        return subscriberRepository.findAllByUserId(userId).stream()
                .map(Subscriber::getSubscriber)
                .map(eventService::getEventByInitiator)
                .filter(event -> event.getState().equals(EventState.PENDING)
                                 || event.getState().equals(EventState.PUBLISHED))
                .map(listConverter::convert)
                .toList();
    }

    private User getUser(long userId, long subscriberId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя не существует"));
        return userRepository.findById(subscriberId).orElseThrow(() -> new NotFoundException("Пользователя не существует"));
    }
}
