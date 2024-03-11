package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;

public class NotSupportedOperationException extends APIException{
    public NotSupportedOperationException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}