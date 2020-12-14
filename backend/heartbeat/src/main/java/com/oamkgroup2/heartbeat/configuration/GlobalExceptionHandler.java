package com.oamkgroup2.heartbeat.configuration;

import java.util.logging.Logger;

import com.oamkgroup2.heartbeat.exception.DuplicateEntityException;
import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler({ IllegalArgumentException.class })
    public final ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
        LOG.info("called illegalargument!");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String body = "Illegal Argument: " + ex.getMessage();
        ApiError error = new ApiError(status, body, ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({ DuplicateEntityException.class })
    public final ResponseEntity<Object> handleDuplicateException(Exception ex) {
        LOG.info("called handleduplicate!");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String body = "Entity already exists: " + ex.getMessage();
        ApiError error = new ApiError(status, body, ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public final ResponseEntity<Object> handleNotFoundException(Exception ex) {
        LOG.info("called entitynotfound!");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String body = "Entity not found: " + ex.getMessage();
        ApiError error = new ApiError(status, body, ex);
        return buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        LOG.info("request rejected: " + apiError);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
