package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.exceptions.BadCredentialsRuntimeException;
import com.ls.sistemavendas.exceptions.EventAtSameTimeRuntimeException;
import com.ls.sistemavendas.exceptions.ParticipantCodeAlreadyUsedRuntimeException;
import com.ls.sistemavendas.exceptions.EventRepeatedRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            ConstraintViolationException ex ) {
        Map<String, String> constraintViolations = new HashMap<>();
        ex.getConstraintViolations().forEach((constraintViolation) -> {
            String message = constraintViolation.getMessage();
            String field = constraintViolation.getPropertyPath().toString();
            field = field.substring(field.lastIndexOf('.')+1); // returns the last component of the path
            constraintViolations.put(field, message);
        });
        return ResponseEntity.badRequest().body(constraintViolations);
    }

    @ExceptionHandler(value = {EventRepeatedRuntimeException.class})
    protected ResponseEntity<Object> handleEventRepeatedException(
            EventRepeatedRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {EventAtSameTimeRuntimeException.class})
    protected ResponseEntity<Object> handleEventAtSameTimeException(
            EventAtSameTimeRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {BadCredentialsRuntimeException.class})
    protected ResponseEntity<Object> handleBadCredentialsException(
            BadCredentialsRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {ParticipantCodeAlreadyUsedRuntimeException.class})
    protected ResponseEntity<Object> handleParticipantCodeAlreadyUsedException(
            ParticipantCodeAlreadyUsedRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
