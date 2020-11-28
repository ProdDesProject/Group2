package com.oamkgroup2.heartbeat.configuration;

import com.oamkgroup2.heartbeat.exception.DuplicateEntityException;
import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import io.micrometer.core.ipc.http.HttpSender.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ IllegalArgumentException.class })
    public final ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String body = "Illegal Argument: " + ex.getMessage();

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({ DuplicateEntityException.class })
    public final ResponseEntity<String> handleDuplicateException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String body = "Entity already exists: " + ex.getMessage();

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public final ResponseEntity<String> handleNotFoundException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String body = "Entity not found: " + ex.getMessage();

        return new ResponseEntity<>(body, headers, status);
    }

}
