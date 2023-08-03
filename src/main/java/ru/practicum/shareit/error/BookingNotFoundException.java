package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Booking not found")
public class BookingNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public BookingNotFoundException() {
        super();
    }

    public BookingNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BookingNotFoundException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BookingNotFoundException(final String message) {
        super(message);
    }

    public BookingNotFoundException(final Throwable cause) {
        super(cause);
    }
}
