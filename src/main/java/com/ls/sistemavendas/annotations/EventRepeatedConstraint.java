package com.ls.sistemavendas.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventRepeatedValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventRepeatedConstraint {
    String message() default "Use outro nome de evento! Porque já existe um evento com este nome.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}


