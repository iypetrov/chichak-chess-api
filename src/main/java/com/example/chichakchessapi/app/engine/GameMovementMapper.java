package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.engine.dtos.GameMovementRequestDTO;
import com.example.chichakchessapi.app.engine.models.GameMovementModel;

public class GameMovementMapper {
    private GameMovementMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static GameMovementModel convertGameMovementRequestDTOToGameMovementRequest(GameMovementRequestDTO gameMovementRequest) {
        return new GameMovementModel(
                gameMovementRequest.gameID(),
                gameMovementRequest.playerID(),
                gameMovementRequest.movement()
        );
    }
}