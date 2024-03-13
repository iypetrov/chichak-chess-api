package com.example.chichakchessapi.app.playerpreferences;

import com.example.chichakchessapi.app.playerpreferences.dtos.PlayerPreferenceResponseDTO;
import com.example.chichakchessapi.app.playerpreferences.models.PlayerPreferenceModel;

public class PlayerPreferenceMapper {
    private PlayerPreferenceMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static PlayerPreferenceResponseDTO convertPlayerPreferenceModelToPlayerPreferenceResponseDTO(PlayerPreferenceModel playerPreference) {
        return new PlayerPreferenceResponseDTO(
                playerPreference.getId(),
                playerPreference.getBrightColor(),
                playerPreference.getDarkColor()
        );
    }
}