package com.example.chichakchessapi.app.common;

import java.util.Optional;
import java.util.UUID;

public class UUIDUtilService {
    private UUIDUtilService() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<String> convertFromStringToUUID(String id) {
        try {
            return Optional.of(UUID.fromString(id).toString());
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
