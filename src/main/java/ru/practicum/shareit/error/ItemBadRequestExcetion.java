package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error request")
public class ItemBadRequestExcetion  extends RuntimeException {
    HttpStatus httpStatus;

    public ItemBadRequestExcetion() {
        super();
    }

    public ItemBadRequestExcetion(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ItemBadRequestExcetion(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ItemBadRequestExcetion(final String message) {
        super(message);
    }

    public ItemBadRequestExcetion(final Throwable cause) {
        super(cause);
    }
}
