package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User with same email exists")
public class UserConflictException extends RuntimeException {
    HttpStatus httpStatus;

    public UserConflictException() {
        super();
    }

    public UserConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserConflictException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UserConflictException(final String message) {
        super(message);
    }

    public UserConflictException(final Throwable cause) {
        super(cause);
    }
}
