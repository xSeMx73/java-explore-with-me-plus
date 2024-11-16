package ru.practicum.evm.event.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<ValidEventDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext context) {
        if (eventDate == null) {
            return true; // или false, в зависимости от Вашей логики
        }
        LocalDateTime nowPlusTwoHours = LocalDateTime.now().plusHours(2);
        return eventDate.isAfter(nowPlusTwoHours) || eventDate.isEqual(nowPlusTwoHours);
    }
}
