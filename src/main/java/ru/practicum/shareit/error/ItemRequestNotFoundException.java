package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Item request not found")
public class ItemRequestNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public ItemRequestNotFoundException() {
        super();
    }

}
