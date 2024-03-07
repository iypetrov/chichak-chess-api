package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.auth.models.LoginModel;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.players.PlayerService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthService extends BaseService {
    public static final long TOKEN_VALIDITY_SECS = 14400L;
    public static final String COOKIE_AUTH_TOKEN_NAME = "AUTH-TOKEN";

    private final Key jwtKey;

    private final PlayerService playerService;

    public AuthService(
            @Value("${app.jwt.secret}") String jwtSecret,
            PlayerService playerService
    ) {
        this.jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.playerService = playerService;
    }

    public RegisterModel convertRegisterRequestDTOToRegisterModel(RegisterRequestDTO registerRequestDTO) {
        return map(registerRequestDTO, RegisterModel.class);
    }

    public LoginModel convertLoginRequestDTOToLoginModel(LoginRequestDTO loginRequestDTO) {
        return map(loginRequestDTO, LoginModel.class);
    }

    public PlayerModel register(RegisterModel registerModel) {
        return playerService.createPlayer(registerModel);
    }

    public PlayerModel login(LoginModel loginModel) {
        return playerService.getPlayerByEmail(loginModel.getEmail());
    }
}
