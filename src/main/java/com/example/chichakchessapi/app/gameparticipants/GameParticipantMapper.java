package com.example.chichakchessapi.app.gameparticipants;

import com.example.chichakchessapi.app.gameparticipants.dtos.GameParticipantResponseDTO;
import com.example.chichakchessapi.app.gameparticipants.entities.GameParticipantEntity;
import com.example.chichakchessapi.app.gameparticipants.models.GameParticipantModel;
import com.example.chichakchessapi.app.games.GameMapper;
import com.example.chichakchessapi.app.players.PlayerMapper;

import java.util.ArrayList;
import java.util.List;

public class GameParticipantMapper {
    private GameParticipantMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static List<GameParticipantResponseDTO> convertGameParticipantModelsToGameParticipantResponseDTOs(List<GameParticipantModel> gameParticipants) {
        List<GameParticipantResponseDTO> gameParticipantDTOs = new ArrayList<>();
        gameParticipants.forEach(gameParticipant -> gameParticipantDTOs.add(
                        new GameParticipantResponseDTO(
                                gameParticipant.getId(),
                                gameParticipant.getGame().getId(),
                                gameParticipant.getPlayer().getId(),
                                gameParticipant.getIsWinner(),
                                gameParticipant.getIsDraw()
                        )
                )
        );
        return gameParticipantDTOs;
    }

    public static List<GameParticipantModel> convertGameParticipantEntitiesToGameParticipantModels(List<GameParticipantEntity> gameParticipants) {
        List<GameParticipantModel> gameParticipantModels = new ArrayList<>();
        gameParticipants.forEach(gameParticipant -> gameParticipantModels.add(
                        new GameParticipantModel(
                                gameParticipant.getId(),
                                GameMapper.convertGameEntityToGameModel(gameParticipant.getGame()),
                                PlayerMapper.convertPlayerEntityToPlayerModel(gameParticipant.getPlayer()),
                                gameParticipant.getIsWinner(),
                                gameParticipant.getIsDraw()
                        )
                )
        );
        return gameParticipantModels;
    }

}