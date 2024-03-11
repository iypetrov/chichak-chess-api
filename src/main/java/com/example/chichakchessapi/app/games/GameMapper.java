package com.example.chichakchessapi.app.games;

import com.example.chichakchessapi.app.games.dtos.GameResponseDTO;
import com.example.chichakchessapi.app.games.entities.GameEntity;
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

    public static GameModel convertGameEntityToGameModel(GameEntity game) {
        return new GameModel(
                game.getId(),
                game.getFullmoveNumber(),
                game.getCreatedOn(),
                game.getFinishedOn()
        );
    }
}