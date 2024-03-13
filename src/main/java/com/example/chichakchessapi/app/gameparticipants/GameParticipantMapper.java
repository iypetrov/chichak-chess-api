package com.example.chichakchessapi.app.gameparticipants;

import com.example.chichakchessapi.app.gameparticipants.dtos.GameParticipantResponseDTO;
import com.example.chichakchessapi.app.gameparticipants.models.GameParticipantModel;

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
                                gameParticipant.getColor().toString(),
                                gameParticipant.getIsWinner(),
                                gameParticipant.getIsDraw()
                        )
                )
        );
        return gameParticipantDTOs;
    }
}