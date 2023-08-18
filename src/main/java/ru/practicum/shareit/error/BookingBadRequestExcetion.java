package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error request")
public class BookingBadRequestExcetion extends RuntimeException {
    HttpStatus httpStatus;

    public BookingBadRequestExcetion() {
        super();
    }
}
