package ru.practicum.shareit.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
        ErrorResponse errors = new ErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            ErrorItem error = new ErrorItem();
            error.setCode(violation.getMessageTemplate());
            error.setMessage(violation.getPropertyPath() + " - " + violation.getMessage());
            errors.addError(error);
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorItem> handle(MissingRequestHeaderException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        log.info(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorItem> handle(NullPointerException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        log.info(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorItem> handle(ResourceNotFoundException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        log.info(e.getMessage());
        return new ResponseEntity<>(error, e.httpStatus);
    }

    public static class ErrorItem {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String code;

        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public static class ErrorResponse {

        private List<ErrorItem> errors = new ArrayList<>();

        public List<ErrorItem> getErrors() {
            return errors;
        }

        public void setErrors(List<ErrorItem> errors) {
            this.errors = errors;
        }

        public void addError(ErrorItem error) {
            this.errors.add(error);
        }

    }
}
