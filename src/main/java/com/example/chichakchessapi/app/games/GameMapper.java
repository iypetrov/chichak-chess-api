package com.example.chichakchessapi.app.games;

import com.example.chichakchessapi.app.games.dtos.GameResponseDTO;
import com.example.chichakchessapi.app.games.models.GameModel;

import java.util.Objects;

public class GameMapper {
    private GameMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static GameResponseDTO convertGameModelToGameResponseDTO(GameModel game) {
        if (Objects.isNull(game)) {
            return new GameResponseDTO(null, null, null, null);
        }

        return new GameResponseDTO(
                game.getId(),
                game.getFullmoveNumber(),
                game.getCreatedOn(),
                game.getFinishedOn()
        );
    }
}