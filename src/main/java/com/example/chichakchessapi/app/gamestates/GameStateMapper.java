package com.example.chichakchessapi.app.gamestates;

import com.example.chichakchessapi.app.games.GameMapper;
import com.example.chichakchessapi.app.gamestates.dtos.GameStateResponseDTO;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameStateMapper {
    private GameStateMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static GameStateResponseDTO convertGameModelToGameResponseDTO(GameStateModel gameState) {
        return new GameStateResponseDTO(
                gameState.getId(),
                GameMapper.convertGameModelToGameResponseDTO(gameState.getGame()),
                gameState.getBoardState(),
                Objects.isNull(gameState.getActiveColor()) ? null : gameState.getActiveColor().toString(),
                gameState.getCastleAvailability(),
                gameState.getEnPassantTargetSquare(),
                gameState.getHalfmoveClock(),
                gameState.getFullmoveNumber(),
                gameState.getIsFinal(),
                gameState.getCreatedOn()
        );
    }

    public static List<GameStateResponseDTO> convertGameModelsToGameResponseDTOs(List<GameStateModel> gameStates) {
        List<GameStateResponseDTO> gameStateResponseDTOs = new ArrayList<>();
        gameStates.forEach(gameState -> gameStateResponseDTOs.add(
                        new GameStateResponseDTO(
                                gameState.getId(),
                                GameMapper.convertGameModelToGameResponseDTO(gameState.getGame()),
                                gameState.getBoardState(),
                                gameState.getActiveColor().toString(),
                                gameState.getCastleAvailability(),
                                gameState.getEnPassantTargetSquare(),
                                gameState.getHalfmoveClock(),
                                gameState.getFullmoveNumber(),
                                gameState.getIsFinal(),
                                gameState.getCreatedOn()
                        )
                )
        );
        return gameStateResponseDTOs;
    }
}