package com.example.chichakchessapi.app.players.dtos;

import com.example.chichakchessapi.app.playerpreferences.dtos.PlayerPreferenceResponseDTO;

public record PlayerResponseDTO(String id, String nickname, String email, String role, String imageURL, Integer points, PlayerPreferenceResponseDTO playerPreference) {
}