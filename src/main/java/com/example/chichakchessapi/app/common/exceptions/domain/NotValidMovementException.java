package com.example.chichakchessapi.app.common.exceptions.domain;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;
import com.example.chichakchessapi.app.common.exceptions.APIException;

public class NotValidMovementException extends APIException {
    public NotValidMovementException(String message, APIErrorResponse apiErrorResponse) {
        super(message, apiErrorResponse);
    }
}
