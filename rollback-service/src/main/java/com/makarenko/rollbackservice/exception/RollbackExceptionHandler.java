package com.makarenko.rollbackservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RollbackExceptionHandler {

    @ExceptionHandler({RollbackException.class})
    public ResponseEntity<?> handleRuntimeException(RollbackException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
