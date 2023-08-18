package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Item not found")
public class ItemNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public ItemNotFoundException() {
        super();
    }

}
