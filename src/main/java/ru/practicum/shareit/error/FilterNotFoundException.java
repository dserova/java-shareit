package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Unknown state: ")
public class FilterNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public FilterNotFoundException() {
        super();
    }

    public FilterNotFoundException(final String message) {
        super(message);
    }

}
