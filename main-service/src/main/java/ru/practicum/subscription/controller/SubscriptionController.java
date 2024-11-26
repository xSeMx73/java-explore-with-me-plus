package ru.practicum.subscription.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.exception.ConditionsNotMetException;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.model.BlackList;
import ru.practicum.subscription.model.Subscriber;
import ru.practicum.subscription.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions/{subscriberId}")
    public void addSubscribe(@PathVariable("userId") @Positive @NotNull long userId,
                             @PathVariable("subscriberId") @Positive @NotNull long subscriberId) {
        System.out.println("Request to add subscriber " + subscriberId + " to user " + userId);
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(userId);
        subscriber.setSubscriber(subscriberId);
        subscriptionService.addSubscriber(subscriber);
    }

    @PostMapping("black-list/{blackListId}")
    public void addBlackList(@PathVariable("userId") @NotBlank long userId,
                             @PathVariable("blackListId") @NotBlank long blackListId) {
        if (userId == blackListId) {
            throw new ConditionsNotMetException("Пользователь не может подписаться сам на себя");
        }
        BlackList blackList = new BlackList();
        blackList.setUserId(userId);
        blackList.setBlackList(blackListId);
        subscriptionService.addBlacklist(blackList);

    }

    @DeleteMapping("/subscriptions/{subscriberId}")
    public void removeSubscriber(@PathVariable("userId") @NotBlank long userId,
                                 @PathVariable("subscriberId") @NotBlank long subscriberId) {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(userId);
        subscriber.setSubscriber(subscriberId);
    }

    @DeleteMapping("/black-list/{blackListId}")
    public void removeBlackList(@PathVariable("userId") @NotBlank long userId,
                                @PathVariable("blackListId") @NotBlank long blackListId) {
        BlackList blackList = new BlackList();
        blackList.setUserId(userId);
        blackList.setBlackList(blackListId);
    }

    @GetMapping("/subscriptions")
    public SubscriptionDto getListSubscriptions(@PathVariable("userId") @Positive long userId) {
        return subscriptionService.getSubscribers(userId);
    }

    @GetMapping("/black-list")
    public SubscriptionDto getBlackListSubscriptions(@PathVariable("userId") @Positive long userId) {
        return subscriptionService.getBlacklists(userId);
    }

    @GetMapping("/subscriptions/events")
    public List<EventShortResponseDto> getEventsSubscriptions(@PathVariable("userId") @Positive long userId) {
        return subscriptionService.getEvents(userId);
    }
}
