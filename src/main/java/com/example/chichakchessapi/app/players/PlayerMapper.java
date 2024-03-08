package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
import com.example.chichakchessapi.app.players.models.PlayerModel;

import java.util.ArrayList;
import java.util.List;

public class PlayerMapper {
    private PlayerMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static PlayerResponseDTO convertPlayerModelToPlayerResponseDTO(PlayerModel playerModel) {
        return new PlayerResponseDTO(
                playerModel.getId(),
                playerModel.getNickname(),
                playerModel.getEmail(),
                playerModel.getRole(),
                playerModel.getImageURL(),
                playerModel.getPoints()
        );
    }

    public static List<PlayerResponseDTO> convertPlayerModelsToPlayerResponseDTOs(List<PlayerModel> playerModels) {
        List<PlayerResponseDTO> playerResponseDTOs = new ArrayList<>();
        playerModels.forEach(playerModel -> playerResponseDTOs.add(
                        new PlayerResponseDTO(
                                playerModel.getId(),
                                playerModel.getNickname(),
                                playerModel.getEmail(),
                                playerModel.getRole(),
                                playerModel.getImageURL(),
                                playerModel.getPoints()
                        )
                )
        );
        return playerResponseDTOs;
    }
}
