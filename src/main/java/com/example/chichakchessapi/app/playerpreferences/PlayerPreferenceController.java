package com.example.chichakchessapi.app.playerpreferences;

import com.example.chichakchessapi.app.playerpreferences.dtos.PlayerPreferenceRequestDTO;
import com.example.chichakchessapi.app.playerpreferences.dtos.PlayerPreferenceResponseDTO;
import com.example.chichakchessapi.app.playerpreferences.models.PlayerPreferenceModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player-preferences")
public class PlayerPreferenceController {
    private final PlayerPreferenceService playerPreferenceService;

    public PlayerPreferenceController(PlayerPreferenceService playerPreferenceService) {
        this.playerPreferenceService = playerPreferenceService;
    }

    @PutMapping
    public ResponseEntity<PlayerPreferenceResponseDTO> updatePlayerPreference(@RequestBody PlayerPreferenceRequestDTO playerPreferenceRequest) {
        PlayerPreferenceModel playerPreference = playerPreferenceService.updatePlayerPreference(
                playerPreferenceRequest.playerID(),
                playerPreferenceRequest.brightColor(),
                playerPreferenceRequest.darkColor()
        );
        return ResponseEntity.ok().body(
                PlayerPreferenceMapper.convertPlayerPreferenceModelToPlayerPreferenceResponseDTO(playerPreference)
        );
    }
}