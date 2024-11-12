package ru.practicum.evm.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.evm.category.model.Category;
import ru.practicum.evm.event.enums.EventState;
import ru.practicum.evm.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@EqualsAndHashCode(of = "id")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User initiator;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Category category;

    @NotNull
    private LocalDateTime eventDate;

    @Embedded
    @NotNull
    private Location location;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @PositiveOrZero
    private long participantLimit;

    private boolean paid;
    private boolean requestModeration;

    @Transient
    private long confirmedRequests;

    @Transient
    private long views;

    @NotNull
    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventState state;
}
