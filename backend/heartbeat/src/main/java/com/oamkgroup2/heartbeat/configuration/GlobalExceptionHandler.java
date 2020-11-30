package com.oamkgroup2.heartbeat.configuration;

import com.oamkgroup2.heartbeat.exception.DuplicateEntityException;
import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ IllegalArgumentException.class })
    public final ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String body = "Illegal Argument: " + ex.getMessage();
        ApiError error = new ApiError(status, body, ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({ DuplicateEntityException.class })
    public final ResponseEntity<Object> handleDuplicateException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String body = "Entity already exists: " + ex.getMessage();
        ApiError error = new ApiError(status, body, ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public final ResponseEntity<Object> handleNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String body = "Entity not found: " + ex.getMessage();
        ApiError error = new ApiError(status, body, ex);
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
