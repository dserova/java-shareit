package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Booking not found")
public class BookingNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public BookingNotFoundException() {
        super();
    }
}
