package com.example.chichakchessapi.app.gamestates;

import com.example.chichakchessapi.app.gamestates.dtos.GameStateResponseDTO;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("game-states")
public class GameStateController {
    private final GameStateService gameStateService;

    public GameStateController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @GetMapping
    public ResponseEntity<List<GameStateResponseDTO>> getAllByGameID(
            @NotNull @RequestParam String gameID
    ) {
        List<GameStateModel> gameStates = gameStateService.getGameStatesByGameID(gameID);
        return ResponseEntity.ok().body(
                GameStateMapper.convertGameModelsToGameResponseDTOs(gameStates)
        );
    }
}