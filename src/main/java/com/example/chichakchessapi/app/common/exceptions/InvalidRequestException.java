package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;

public class InvalidRequestException extends APIException{
    public InvalidRequestException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}