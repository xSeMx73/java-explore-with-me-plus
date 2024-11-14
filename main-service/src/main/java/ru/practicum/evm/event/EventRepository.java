package ru.practicum.evm.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategoryId(long categoryId);
}
