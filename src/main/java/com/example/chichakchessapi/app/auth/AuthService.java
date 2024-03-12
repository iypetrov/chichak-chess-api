package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.models.LoginModel;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.players.PlayerFindService;
import com.example.chichakchessapi.app.players.PlayerService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService extends BaseService {
    public static final long TOKEN_VALIDITY_SECS = 14400L;
    public static final String COOKIE_AUTH_TOKEN_NAME = "AUTH-TOKEN";
    public static final String ROLE_CLAIM_NAME = "role";


    private final PlayerService playerService;
    private final PlayerFindService playerFindService;
    private final JWTGenerationService jwtGenerationService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            PlayerService playerService,
            PlayerFindService playerFindService,
            JWTGenerationService jwtGenerationService,
            PasswordEncoder passwordEncoder
    ) {
        this.playerService = playerService;
        this.playerFindService = playerFindService;
        this.jwtGenerationService = jwtGenerationService;
        this.passwordEncoder = passwordEncoder;
    }

    public PlayerModel register(RegisterModel registration) {
        PlayerModel player = playerService.createPlayer(registration);
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIM_NAME, player.getRole());

        player.setJwtToken(
                jwtGenerationService.generateJWTToken(
                        claims,
                        player
                )
        );

        return player;
    }

    public PlayerModel login(LoginModel login) {
        PlayerModel player = playerFindService.getPlayerByEmail(login.getEmail());
        String encodedPassword = playerService.getPlayersEncodedPasswordByID(player.getId());
        if (!passwordEncoder.matches(login.getPassword(), encodedPassword)) {
            throw unauthorized(
                    CustomMessageUtil.PLAYER_WRONG_PASSWORD,
                    CustomMessageUtil.PLAYER_ID + player.getId()
            ).get();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIM_NAME, player.getRole());

        player.setJwtToken(
                jwtGenerationService.generateJWTToken(
                        claims,
                        player
                )
        );

        return player;
    }
}