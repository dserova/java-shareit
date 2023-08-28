package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private final String handleName = "handle";

    private ErrorItem handleCustomErrorItem(String responseStatusReason, HttpStatus responseStatus) {
        ErrorItem error = new ErrorItem();
        error.setMessage(responseStatusReason);
        error.setCode(responseStatus.toString());
        log.info(error.toString());
        return error;
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ConstraintViolationException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        ErrorResponse errors = new ErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            errors.addError(handleCustomErrorItem(
                    violation.getPropertyPath() + " - " + violation.getMessage(),
                    currentMethod.getAnnotation(ResponseStatus.class).value()
            ));
        }
        log.info(errors.toString());
        return errors;
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MissingRequestHeaderException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getMessage(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorItem handle(DataIntegrityViolationException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "Request error",
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(NullPointerException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getMessage(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(BookingBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(BookingBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(PageableBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(PageableBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(FilterNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(FilterNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason() + e.getMessage(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MethodArgumentTypeMismatchException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "Unknown " + e.getName() + ": " + Objects.requireNonNull(e.getValue()),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }
}
