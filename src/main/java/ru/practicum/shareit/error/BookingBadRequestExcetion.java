package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error request")
public class BookingBadRequestExcetion extends RuntimeException {
    HttpStatus httpStatus;

    public BookingBadRequestExcetion() {
        super();
    }

    public BookingBadRequestExcetion(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BookingBadRequestExcetion(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BookingBadRequestExcetion(final String message) {
        super(message);
    }

    public BookingBadRequestExcetion(final Throwable cause) {
        super(cause);
    }
}
