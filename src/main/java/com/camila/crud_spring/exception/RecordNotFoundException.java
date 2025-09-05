package com.camila.crud_spring.exception;

public class RecordNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(String message, Long id) {
        super(message + id);
    }
}
