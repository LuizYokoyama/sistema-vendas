package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.ClientErrorException;
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
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(value = {EventAtSameTimeRuntimeException.class})
    protected ResponseEntity<Object> handleEventAtSameTimeException(
            EventAtSameTimeRuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(value = {BadCredentialsRuntimeException.class})
    protected ResponseEntity<Object> handleBadCredentialsException(
            BadCredentialsRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {ParticipantCodeAlreadyUsedRuntimeException.class})
    protected ResponseEntity<Object> handleParticipantCodeAlreadyUsedException(
            ParticipantCodeAlreadyUsedRuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(value = {StandNotFoundRuntimeException.class})
    protected ResponseEntity<Object> handleStandNotFoundException(
            StandNotFoundRuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = {ParticipantCodeNotFoundRuntimeException.class})
    protected ResponseEntity<Object> handleParticipantCodeNotFoundException(
            ParticipantCodeNotFoundRuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = {    UserNameAlreadyExistsRuntimeException.class})
    protected ResponseEntity<Object> handleUserNameAlreadyExistsException(
            UserNameAlreadyExistsRuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(value = {    RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(value = {    EventNotFoundRuntimeException.class})
    protected ResponseEntity<Object> handleEventNotFoundRuntimeException(
            EventNotFoundRuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = {    ClientErrorException.class})
    protected ResponseEntity<Object> handleClientErrorException(
            ClientErrorException  ex ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Use um login diferente! Porque este já foi usado.");
    }

    @ExceptionHandler(value = {    AgentCodeNotFoundRuntimeException.class})
    protected ResponseEntity<Object> handleAgentCodeNotFoundException(
            AgentCodeNotFoundRuntimeException  ex ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
