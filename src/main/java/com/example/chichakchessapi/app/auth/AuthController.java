package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.players.PlayerMapper;
import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerResponseDTO> register(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        PlayerModel playerModel = authService.register(
                AuthMapper.convertRegisterRequestDTOToRegisterModel(registerRequestDTO)
        );

        ResponseCookie cookieAuthToken = authService.getCookie(
                AuthService.COOKIE_AUTH_TOKEN_NAME,
                playerModel.getJwtToken(),
                AuthService.TOKEN_VALIDITY_SECS
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieAuthToken.toString())
                .body(PlayerMapper.convertPlayerModelToPlayerResponseDTO(playerModel));
    }

    @PostMapping("/login")
    public ResponseEntity<PlayerResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        PlayerModel playerModel = authService.login(
                AuthMapper.convertLoginRequestDTOToLoginModel(loginRequestDTO)
        );

        ResponseCookie cookieAuthToken = authService.getCookie(
                AuthService.COOKIE_AUTH_TOKEN_NAME,
                playerModel.getJwtToken(),
                AuthService.TOKEN_VALIDITY_SECS
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieAuthToken.toString())
                .body(PlayerMapper.convertPlayerModelToPlayerResponseDTO(playerModel));
    }
}
