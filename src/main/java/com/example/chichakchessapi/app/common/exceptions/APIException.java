package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private final APIErrorResponse apiErrorResponse;

    public APIException(String message, APIErrorResponse apiErrorResponse) {
        super(message);
        this.apiErrorResponse = apiErrorResponse;
    }
}