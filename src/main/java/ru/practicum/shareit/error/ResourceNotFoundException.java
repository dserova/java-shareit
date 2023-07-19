package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }
}
