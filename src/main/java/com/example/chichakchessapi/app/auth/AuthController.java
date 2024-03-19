package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.players.PlayerMapper;
import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private ResponseCookie getCookie(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .maxAge(maxAgeSeconds)
                .path("/")
                .secure(false)
                .httpOnly(false)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerResponseDTO> register(
            @RequestBody RegisterRequestDTO registerRequest
    ) {
        PlayerModel player = authService.register(
                AuthMapper.convertRegisterRequestDTOToRegisterModel(registerRequest)
        );

        ResponseCookie cookieAuthToken = getCookie(
                AuthService.COOKIE_AUTH_TOKEN_NAME,
                player.getJwtToken(),
                AuthService.TOKEN_VALIDITY_SECS
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookieAuthToken.toString())
                .body(PlayerMapper.convertPlayerModelToPlayerResponseDTO(player));
    }

    @PostMapping("/login")
    public ResponseEntity<PlayerResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequest
    ) {
        PlayerModel player = authService.login(
                AuthMapper.convertLoginRequestDTOToLoginModel(loginRequest)
        );

        ResponseCookie cookieAuthToken = getCookie(
                AuthService.COOKIE_AUTH_TOKEN_NAME,
                player.getJwtToken(),
                AuthService.TOKEN_VALIDITY_SECS
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieAuthToken.toString())
                .body(PlayerMapper.convertPlayerModelToPlayerResponseDTO(player));

    }
    @PostMapping("/logout")
    public ResponseEntity<PlayerResponseDTO> logout() {
        ResponseCookie cookieWithoutAuthToken = getCookie(
                AuthService.COOKIE_AUTH_TOKEN_NAME,
                "",
                AuthService.TOKEN_VALIDITY_SECS
        );

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieWithoutAuthToken.toString())
                .build();
    }
}