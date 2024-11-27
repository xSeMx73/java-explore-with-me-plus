package ru.practicum.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.subscription.model.BlackList;

import java.util.List;
import java.util.Optional;


public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    List<BlackList> findAllByUserId(Long userId);

    void deleteByUserIdAndBlockUser(Long userId, Long blackListId);

    Optional<BlackList> findByUserIdAndBlockUser(Long userId, Long blackListId);
}
