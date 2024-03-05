package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;

public class UnauthorizedException extends APIException{
    public UnauthorizedException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}
