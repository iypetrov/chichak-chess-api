package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.engine.dtos.GameMovementRequestDTO;
import com.example.chichakchessapi.app.gamestates.GameStateMapper;
import com.example.chichakchessapi.app.gamestates.dtos.GameStateResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.chichakchessapi.app.auth.AuthService.COOKIE_AUTH_TOKEN_NAME;

@RestController
@RequestMapping("/game")
public class ChessMovementController {
    private final ChessMovementService chessMovementService;
    private final GameCurrentStateService gameCurrentStateService;

    public ChessMovementController(ChessMovementService chessMovementService, GameCurrentStateService gameCurrentStateService) {
        this.chessMovementService = chessMovementService;
        this.gameCurrentStateService = gameCurrentStateService;
    }

    @PostMapping("/movement")
    public ResponseEntity<GameStateResponseDTO> makeTurn(
            @CookieValue(name = COOKIE_AUTH_TOKEN_NAME) String jwtToken,
            @RequestBody GameMovementRequestDTO gameMovementRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        GameStateMapper.convertGameModelToGameResponseDTO(
                                chessMovementService.addPlayerMovement(
                                        GameMovementMapper.convertGameMovementRequestDTOToGameMovementRequest(
                                                gameMovementRequest
                                        ),
                                        jwtToken
                                )
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameStateResponseDTO> getLatestGameState(
            @PathVariable String id
    ) {
        return ResponseEntity.ok()
                .body(
                        GameStateMapper.convertGameModelToGameResponseDTO(
                                gameCurrentStateService.getLatestGameStateByGameID(id)
                        )
                );
    }

    @PostMapping("/surrender/{id}")
    public ResponseEntity<Void> surrender(
            @PathVariable String id,
            @CookieValue(name = COOKIE_AUTH_TOKEN_NAME) String jwtToken
    ) {
        chessMovementService.surrenderPlayer(id, jwtToken);
        return ResponseEntity.noContent().build();
    }
}
