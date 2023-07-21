package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;


public class ExceptionBadRequest extends RuntimeException {
    HttpStatus httpStatus;

    public ExceptionBadRequest() {
        super();
    }

    public ExceptionBadRequest(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExceptionBadRequest(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ExceptionBadRequest(final String message) {
        super(message);
    }

    public ExceptionBadRequest(final Throwable cause) {
        super(cause);
    }
}
