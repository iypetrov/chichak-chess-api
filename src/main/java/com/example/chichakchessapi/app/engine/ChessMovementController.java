package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.engine.dtos.GameMovementRequestDTO;
import com.example.chichakchessapi.app.gamestates.GameStateMapper;
import com.example.chichakchessapi.app.gamestates.dtos.GameStateResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.chichakchessapi.app.auth.AuthService.COOKIE_AUTH_TOKEN_NAME;

@RestController
@RequestMapping("/game/movement")
public class ChessMovementController {
    private final ChessMovementService chessMovementService;

    public ChessMovementController(ChessMovementService chessMovementService) {
        this.chessMovementService = chessMovementService;
    }

    @PostMapping
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

    @PostMapping("/surrender/{id}")
    public ResponseEntity<Void> surrender(
            @PathVariable String id
    ) {
        chessMovementService.surrenderPlayer(id);
        return ResponseEntity.noContent().build();
    }
}
