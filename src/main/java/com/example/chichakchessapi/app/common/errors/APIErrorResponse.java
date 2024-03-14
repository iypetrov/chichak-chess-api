package com.example.chichakchessapi.app.common.errors;

public record APIErrorResponse(APIErrorResponseType type, String message, String details) {
}