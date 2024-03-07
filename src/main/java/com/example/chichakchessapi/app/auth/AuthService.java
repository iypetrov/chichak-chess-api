package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.auth.models.LoginModel;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.common.CustomMessageUtilService;
import com.example.chichakchessapi.app.players.PlayerService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthService extends BaseService {
    public static final long TOKEN_VALIDITY_SECS = 14400L;
    public static final String COOKIE_AUTH_TOKEN_NAME = "AUTH-TOKEN";

    private final Key jwtKey;

    private final PlayerService playerService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            @Value("${app.jwt.secret}") String jwtSecret,
            PlayerService playerService, PasswordEncoder passwordEncoder
    ) {
        this.jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.playerService = playerService;
        this.passwordEncoder = passwordEncoder;
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
        PlayerModel playerModel = playerService.getPlayerByEmail(loginModel.getEmail());
        String encodedPassword = playerService.getPlayersEncodedPasswordByID(playerModel.getId());
        if (!passwordEncoder.matches(loginModel.getPassword(), encodedPassword)) {
            throw unauthorized(
                    CustomMessageUtilService.PLAYER_WRONG_PASSWORD,
                    CustomMessageUtilService.PLAYER_ID + playerModel.getId()
            ).get();
        }

        return playerModel;
    }
}
