package ru.practicum.evm.request.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.evm.event.model.Event;
import ru.practicum.evm.request.enums.RequestState;
import ru.practicum.evm.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@EqualsAndHashCode(of = "id")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    Event event;

    @CreationTimestamp
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User requester;

    @Enumerated(EnumType.STRING)
    @NotNull
    RequestState status;
}
