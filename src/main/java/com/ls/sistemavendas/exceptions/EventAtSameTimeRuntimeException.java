package com.ls.sistemavendas.exceptions;

public class EventAtSameTimeRuntimeException extends RuntimeException{

    public EventAtSameTimeRuntimeException(String errorMessage){
        super(errorMessage);
    }
}
