package com.example.chichakchessapi.app.common.exceptions.handlers;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;
import com.example.chichakchessapi.app.common.exceptions.InternalServerException;
import com.example.chichakchessapi.app.common.exceptions.InvalidRequestException;
import com.example.chichakchessapi.app.common.exceptions.NotFoundException;
import com.example.chichakchessapi.app.common.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = InternalServerException.class)
    public ResponseEntity<APIErrorResponse> handleException(InternalServerException ex) {
        log.error("Internal Server Exception with message {}.", ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getApiErrorResponse());
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<APIErrorResponse> handleException(InvalidRequestException ex) {
        log.error("Invalid Request Exception: {}.", ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(ex.getApiErrorResponse());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleException(NotFoundException ex) {
        log.error("Not Found Exception with message {}.", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getApiErrorResponse());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<APIErrorResponse> handleException(UnauthorizedException ex) {
        log.error("Unauthorized Request Exception with message {}.", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getApiErrorResponse());
    }
}
