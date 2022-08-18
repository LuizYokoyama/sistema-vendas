package com.ls.sistemavendas.exceptions;

public class EventRepeatedRuntimeException extends RuntimeException{

    public EventRepeatedRuntimeException(String errorMessage){
        super(errorMessage);
    }
}
