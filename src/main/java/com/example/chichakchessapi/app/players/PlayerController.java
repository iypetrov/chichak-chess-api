package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.chichakchessapi.app.auth.AuthService.COOKIE_AUTH_TOKEN_NAME;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerFindService playerFindService;
    private final PlayerService playerService;

    public PlayerController(PlayerFindService playerFindService, PlayerService playerService) {
        this.playerFindService = playerFindService;
        this.playerService = playerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> getByID(@PathVariable String id) {
        return ResponseEntity.ok(
                PlayerMapper.convertPlayerModelToPlayerResponseDTO(playerFindService.getPlayerByID(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> getAll() {
        return ResponseEntity.ok(
                PlayerMapper.convertPlayerModelsToPlayerResponseDTOs(playerFindService.getAllPlayers())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByID(
            @PathVariable String id
//            @CookieValue(name = COOKIE_AUTH_TOKEN_NAME) String jwtToken
    ) {
        String jwtToken = "";
        playerService.deleteUserByUserByID(id, jwtToken);
        return ResponseEntity.noContent().build();
    }
}