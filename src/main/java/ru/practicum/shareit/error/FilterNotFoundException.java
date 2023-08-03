package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Unknown state: ")
public class FilterNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public FilterNotFoundException() {
        super();
    }

    public FilterNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FilterNotFoundException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public FilterNotFoundException(final String message) {
        super(message);
    }

    public FilterNotFoundException(final Throwable cause) {
        super(cause);
    }
}
