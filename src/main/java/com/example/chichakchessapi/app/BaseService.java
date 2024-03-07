package com.example.chichakchessapi.app;

import com.example.chichakchessapi.app.common.errors.APIErrorResponse;
import com.example.chichakchessapi.app.common.errors.APIErrorResponseType;
import com.example.chichakchessapi.app.common.exceptions.BadFormatException;
import com.example.chichakchessapi.app.common.exceptions.InternalServerException;
import com.example.chichakchessapi.app.common.exceptions.InvalidRequestException;
import com.example.chichakchessapi.app.common.exceptions.NotFoundException;
import com.example.chichakchessapi.app.common.exceptions.NotSupportedOperationException;
import com.example.chichakchessapi.app.common.exceptions.domain.NotValidMovementException;
import com.example.chichakchessapi.app.common.exceptions.UnauthorizedException;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.function.Supplier;

public abstract class BaseService {
    @Resource
    private ModelMapper modelMapper;

    protected <T> T map(Object sourceObject, Class<T> targetClass) {
        return modelMapper.map(sourceObject, targetClass);
    }

    protected <T> List<T> map(List<?> sourceObjects, Class<T> targetClass) {
        return sourceObjects
                .stream()
                .map(obj -> modelMapper.map(obj, targetClass))
                .toList();
    }

    protected Supplier<BadFormatException> badFormat(String message, String details) {
        return () -> new BadFormatException(
                message,
                new APIErrorResponse(APIErrorResponseType.WARNING, message, details)
        );
    }

    protected Supplier<InternalServerException> internalServer(String message, String details) {
        return () -> new InternalServerException(
                message,
                new APIErrorResponse(APIErrorResponseType.WARNING, message, details)
        );
    }

    protected Supplier<InvalidRequestException> invalidRequest(String message, String details) {
        return () -> new InvalidRequestException(
                message,
                new APIErrorResponse(APIErrorResponseType.WARNING, message, details)
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

    protected Supplier<NotValidMovementException> notValidMovement(String message, String details) {
        return () -> new NotValidMovementException(
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