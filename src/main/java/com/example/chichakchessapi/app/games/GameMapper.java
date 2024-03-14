package com.example.chichakchessapi.app.games;

import com.example.chichakchessapi.app.games.dtos.GameResponseDTO;
import com.example.chichakchessapi.app.games.models.GameModel;

public class GameMapper {
    private GameMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static GameResponseDTO convertGameModelToGameResponseDTO(GameModel game) {
        return new GameResponseDTO(
                game.getId(),
                game.getFullmoveNumber(),
                game.getCreatedOn(),
                game.getFinishedOn()
        );
    }
}