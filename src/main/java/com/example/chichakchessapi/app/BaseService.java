package com.example.chichakchessapi.app;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;
import com.example.chichakchessapi.app.common.errors.APIErrorResponseType;
import com.example.chichakchessapi.app.common.exceptions.BadFormatException;
import com.example.chichakchessapi.app.common.exceptions.InternalServerException;
import com.example.chichakchessapi.app.common.exceptions.InvalidRequestException;
import com.example.chichakchessapi.app.common.exceptions.NotFoundException;
import com.example.chichakchessapi.app.common.exceptions.NotSupportedOperationException;
import com.example.chichakchessapi.app.common.exceptions.UnauthorizedException;

import java.util.function.Supplier;

public abstract class BaseService {
    protected Supplier<BadFormatException> badFormat(String message, String details) {
        return () -> new BadFormatException(
                message,
                new APIErrorResponse(APIErrorResponseType.WARNING, message, details)
        );
    }

    protected Supplier<InternalServerException> internalServer(String message, String details) {
        return () -> new InternalServerException(
                message,
                new APIErrorResponse(APIErrorResponseType.ERROR, message, details)
        );
    }

    protected Supplier<InvalidRequestException> invalidRequest(String message, String details) {
        return () -> new InvalidRequestException(
                message,
                new APIErrorResponse(APIErrorResponseType.ERROR, message, details)
        );
    }

    protected Supplier<NotFoundException> notFound(String message, String details) {
        return () -> new NotFoundException(
                message,
                new APIErrorResponse(APIErrorResponseType.WARNING, message, details)
        );
    }

    protected Supplier<NotSupportedOperationException> notSupportedOperation(String message, String details) {
        return () -> new NotSupportedOperationException(
                message,
                new APIErrorResponse(APIErrorResponseType.WARNING, message, details)
        );
    }

    protected Supplier<UnauthorizedException> unauthorized(String message, String details) {
        return () -> new UnauthorizedException(
                message,
                new APIErrorResponse(APIErrorResponseType.WARNING, message, details)
        );
    }
}