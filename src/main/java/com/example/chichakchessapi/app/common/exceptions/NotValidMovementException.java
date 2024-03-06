package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;

public class NotValidMovementException extends APIException{
    public NotValidMovementException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}
