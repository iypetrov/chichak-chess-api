package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
import com.example.chichakchessapi.app.players.entities.PlayerEntity;
import com.example.chichakchessapi.app.players.models.PlayerModel;

import java.util.ArrayList;
import java.util.List;

public class PlayerMapper {
    private PlayerMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static PlayerResponseDTO convertPlayerModelToPlayerResponseDTO(PlayerModel player) {
        return new PlayerResponseDTO(
                player.getId(),
                player.getNickname(),
                player.getEmail(),
                player.getRole().toString(),
                player.getImageURL(),
                player.getPoints()
        );
    }

    public static List<PlayerResponseDTO> convertPlayerModelsToPlayerResponseDTOs(List<PlayerModel> players) {
        List<PlayerResponseDTO> playerResponseDTOs = new ArrayList<>();
        players.forEach(player -> playerResponseDTOs.add(
                        new PlayerResponseDTO(
                                player.getId(),
                                player.getNickname(),
                                player.getEmail(),
                                player.getRole().toString(),
                                player.getImageURL(),
                                player.getPoints()
                        )
                )
        );
        return playerResponseDTOs;
    }

    public static PlayerModel convertPlayerEntityToPlayerModel(PlayerEntity player) {
        return new PlayerModel(
                player.getId(),
                player.getNickname(),
                player.getEmail(),
                player.getRole(),
                player.getImageURL(),
                player.getPoints(),
                ""
        );
    }
}