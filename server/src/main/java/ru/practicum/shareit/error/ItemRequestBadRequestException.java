package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error request")
public class ItemRequestBadRequestException extends RuntimeException {
    HttpStatus httpStatus;

    public ItemRequestBadRequestException() {
        super();
    }

}
