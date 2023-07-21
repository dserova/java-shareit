package ru.practicum.shareit.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ConstraintViolationException e) {
        ErrorResponse errors = new ErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            ErrorItem error = new ErrorItem();
            error.setMessage(violation.getPropertyPath() + " - " + violation.getMessage());
            error.setCode(HttpStatus.BAD_REQUEST.toString());
            errors.addError(error);
        }
        log.info(errors.toString());
        return errors;
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MissingRequestHeaderException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.BAD_REQUEST.toString());
        log.info(error.toString());
        return error;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(NullPointerException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.BAD_REQUEST.toString());
        log.info(error.toString());
        return error;
    }

    @ExceptionHandler(ExceptionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(ExceptionNotFound e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.NOT_FOUND.toString());
        log.info(error.toString());
        return error;
    }

    @ExceptionHandler(ExceptionBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(ExceptionBadRequest e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.BAD_REQUEST.toString());
        log.info(error.toString());
        return error;
    }

    @ExceptionHandler(ExceptionConflict.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorItem handle(ExceptionConflict e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.CONFLICT.toString());
        log.info(error.toString());
        return error;
    }

    @Setter
    @Getter
    public static class ErrorItem {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String code;

        private String message;

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    @Setter
    @Getter
    public static class ErrorResponse {

        private List<ErrorItem> errors = new ArrayList<>();

        public void addError(ErrorItem error) {
            this.errors.add(error);
        }

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
}
