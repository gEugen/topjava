package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private static final String EMAIL_DUPLICATION = "users_unique_email_idx";

    private static final String DATE_TIME_DUPLICATION = "meals_unique_user_datetime_idx";

    @Autowired
    private MessageSource messageSource;

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootCauseMessage = ValidationUtil.getRootCause(e).getMessage().toLowerCase();
        if (!rootCauseMessage.contains(EMAIL_DUPLICATION) && !rootCauseMessage.contains(DATE_TIME_DUPLICATION)) {
            return logAndGetErrorInfo(req, e, true, DATA_ERROR);
        }
        if (rootCauseMessage.contains(EMAIL_DUPLICATION)) {
            return new ErrorInfo(req.getRequestURL(), VALIDATION_ERROR, getDetails(EMAIL_DUPLICATION));
        } else {
            return new ErrorInfo(req.getRequestURL(), VALIDATION_ERROR, getDetails(DATE_TIME_DUPLICATION));
        }
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(BindException.class)
    public ErrorInfo bindingRequestDataError(HttpServletRequest req, Exception e) {
        List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
        String[] details = fieldErrors.stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .toArray(String[]::new);
        logErrorInfo(req, e, false, VALIDATION_ERROR);
        return new ErrorInfo(req.getRequestURL(), VALIDATION_ERROR, details);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        logErrorInfo(req, e, logException, errorType);
        String[] details = new String[1];
        details[0] = ValidationUtil.getRootCause(e).getMessage();
        return new ErrorInfo(req.getRequestURL(), errorType, details);
    }

    private static void logErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
    }

    private String[] getDetails(String duplicationIdentifier) {
        String[] details = new String[1];
        if (EMAIL_DUPLICATION.equals(duplicationIdentifier)) {
            details[0] = "[email] " + messageSource.getMessage("common." + EMAIL_DUPLICATION, null, LocaleContextHolder.getLocale());
        } else {
            details[0] = "[dateTime] " + messageSource.getMessage("common." + DATE_TIME_DUPLICATION, null, LocaleContextHolder.getLocale());
        }
        return details;
    }
}