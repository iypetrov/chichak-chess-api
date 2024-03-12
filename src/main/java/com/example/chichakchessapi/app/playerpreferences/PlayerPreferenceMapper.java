package com.example.chichakchessapi.app.playerpreferences;

import com.example.chichakchessapi.app.playerpreferences.dtos.PlayerPreferenceRequestDTO;
import com.example.chichakchessapi.app.playerpreferences.dtos.PlayerPreferenceResponseDTO;
import com.example.chichakchessapi.app.playerpreferences.entities.PlayerPreferenceEntity;
import com.example.chichakchessapi.app.playerpreferences.models.PlayerPreferenceModel;

public class PlayerPreferenceMapper {
    private PlayerPreferenceMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static PlayerPreferenceRequestDTO convertPlayerPreferenceModelToPlayerPreferenceRequestDTO(PlayerPreferenceModel playerPreference) {
        return new PlayerPreferenceRequestDTO(
                playerPreference.getId(),
                playerPreference.getBrightColor(),
                playerPreference.getDarkColor()
        );
    }

    public static PlayerPreferenceResponseDTO convertPlayerPreferenceModelToPlayerPreferenceResponseDTO(PlayerPreferenceModel playerPreference) {
        return new PlayerPreferenceResponseDTO(
                playerPreference.getId(),
                playerPreference.getBrightColor(),
                playerPreference.getDarkColor()
        );
    }

    public static PlayerPreferenceModel convertPlayerPreferenceEntityToPlayerPreferenceModel(PlayerPreferenceEntity playerPreference) {
        return new PlayerPreferenceModel(
                playerPreference.getId(),
                playerPreference.getBrightColor(),
                playerPreference.getDarkColor()
        );
    }
}