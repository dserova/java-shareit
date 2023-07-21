package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;


public class ExceptionConflict extends RuntimeException {
    HttpStatus httpStatus;

    public ExceptionConflict() {
        super();
    }

    public ExceptionConflict(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExceptionConflict(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ExceptionConflict(final String message) {
        super(message);
    }

    public ExceptionConflict(final Throwable cause) {
        super(cause);
    }
}
