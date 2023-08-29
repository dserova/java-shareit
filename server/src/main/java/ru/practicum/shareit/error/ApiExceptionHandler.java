package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorItem handle(DataIntegrityViolationException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "Request error",
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(UserNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(BookingNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
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

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(CommentNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(ItemNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(ItemBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(ItemBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(ItemRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(ItemRequestNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(ItemRequestBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(ItemRequestBadRequestException e) throws NoSuchMethodException {
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
}
