package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> getByID(@PathVariable String id) {
        return ResponseEntity.ok(
                PlayerMapper.convertPlayerModelToPlayerResponseDTO(playerService.getPlayerByID(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> getAll() {
        return ResponseEntity.ok(
                PlayerMapper.convertPlayerModelsToPlayerResponseDTOs(playerService.getAllPlayers())
        );
    }
}