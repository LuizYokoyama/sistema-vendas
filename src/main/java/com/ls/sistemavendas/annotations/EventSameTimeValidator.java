package com.ls.sistemavendas.annotations;

import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventSameTimeValidator implements ConstraintValidator<EventSameTimeConstraint, LocalDateTime> {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void initialize(EventSameTimeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
        if (eventRepository.getEventByDate(dateTime) == null){
            return true;
        }
        return false;
    }
}
