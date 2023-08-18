package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Item request not found")
public class ItemRequestNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public ItemRequestNotFoundException() {
        super();
    }

    public ItemRequestNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ItemRequestNotFoundException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ItemRequestNotFoundException(final String message) {
        super(message);
    }

    public ItemRequestNotFoundException(final Throwable cause) {
        super(cause);
    }
}
