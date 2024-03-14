package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;

public class BadFormatException extends APIException{
    public BadFormatException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}