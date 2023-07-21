package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;


public class ExceptionNotFound extends RuntimeException {
    HttpStatus httpStatus;

    public ExceptionNotFound() {
        super();
    }

    public ExceptionNotFound(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExceptionNotFound(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ExceptionNotFound(final String message) {
        super(message);
    }

    public ExceptionNotFound(final Throwable cause) {
        super(cause);
    }
}
