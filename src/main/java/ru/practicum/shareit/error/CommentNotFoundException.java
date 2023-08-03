package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Booking not found")
public class CommentNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public CommentNotFoundException() {
        super();
    }

    public CommentNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommentNotFoundException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CommentNotFoundException(final String message) {
        super(message);
    }

    public CommentNotFoundException(final Throwable cause) {
        super(cause);
    }
}
