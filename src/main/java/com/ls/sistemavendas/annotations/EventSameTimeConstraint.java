package com.ls.sistemavendas.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventSameTimeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSameTimeConstraint {
    String message() default "Use outra data para este evento! Porque já existe um evento nesta data.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
