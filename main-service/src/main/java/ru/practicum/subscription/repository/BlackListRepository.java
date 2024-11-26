package ru.practicum.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.subscription.model.BlackList;

import java.util.Collection;
import java.util.Optional;


public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    Collection<BlackList> findAllByUserId(Long userId);

    void deleteByUserIdAndBlockUser(Long userId, Long blackListId);

    Optional<BlackList> findByUserIdAndBlockUser(Long userId, Long blackListId);
}
