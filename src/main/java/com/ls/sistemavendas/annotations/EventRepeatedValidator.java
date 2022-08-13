package com.ls.sistemavendas.annotations;


import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventRepeatedValidator implements ConstraintValidator<EventRepeatedConstraint, String> {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void initialize(EventRepeatedConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String eventName, ConstraintValidatorContext context) {
        if (eventRepository.getEventByName(eventName) == null){
            return true;
        }
        return false;
    }


}

