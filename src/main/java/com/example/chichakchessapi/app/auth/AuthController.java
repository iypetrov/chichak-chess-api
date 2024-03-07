package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.players.PlayerService;
import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    private final PlayerService playerService;

    public AuthController(AuthService authService, PlayerService playerService) {
        this.authService = authService;
        this.playerService = playerService;
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerResponseDTO> register(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        PlayerModel playerModel = authService.register(
                authService.convertRegisterRequestDTOToRegisterModel(registerRequestDTO)
        );
        return ResponseEntity.ok().body(
                playerService.convertPlayerModelToPlayerResponseDTO(playerModel)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<PlayerResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        PlayerModel playerModel = authService.login(
                authService.convertLoginRequestDTOToLoginModel(loginRequestDTO)
        );
        return ResponseEntity.ok().body(
                playerService.convertPlayerModelToPlayerResponseDTO(playerModel)
        );
    }
}
