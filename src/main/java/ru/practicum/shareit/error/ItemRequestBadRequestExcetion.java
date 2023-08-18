package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error request")
public class ItemRequestBadRequestExcetion extends RuntimeException {
    HttpStatus httpStatus;

    public ItemRequestBadRequestExcetion() {
        super();
    }

    public ItemRequestBadRequestExcetion(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ItemRequestBadRequestExcetion(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ItemRequestBadRequestExcetion(final String message) {
        super(message);
    }

    public ItemRequestBadRequestExcetion(final Throwable cause) {
        super(cause);
    }
}
