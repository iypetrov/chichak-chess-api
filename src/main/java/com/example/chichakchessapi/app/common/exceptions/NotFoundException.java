package com.example.chichakchessapi.app.common.exceptions;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;

public class NotFoundException extends APIException {
    public NotFoundException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}
