package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;

public class InternalServerException extends APIException{
    public InternalServerException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}
