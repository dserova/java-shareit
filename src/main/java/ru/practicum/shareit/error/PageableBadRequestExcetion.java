package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error request")
public class PageableBadRequestExcetion extends RuntimeException {
    HttpStatus httpStatus;

    public PageableBadRequestExcetion() {
        super();
    }

    public PageableBadRequestExcetion(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PageableBadRequestExcetion(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public PageableBadRequestExcetion(final String message) {
        super(message);
    }

    public PageableBadRequestExcetion(final Throwable cause) {
        super(cause);
    }
}
