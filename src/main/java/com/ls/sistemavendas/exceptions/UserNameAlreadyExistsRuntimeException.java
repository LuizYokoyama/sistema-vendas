package com.ls.sistemavendas.exceptions;

public class UserNameAlreadyExistsRuntimeException extends RuntimeException {

    public UserNameAlreadyExistsRuntimeException(String message) {
        super(message);
    }
}
