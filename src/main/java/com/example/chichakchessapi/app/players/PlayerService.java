package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.JWTGenerationService;
import com.example.chichakchessapi.app.auth.PlayerRole;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.common.MapperUtil;
import com.example.chichakchessapi.app.common.UUIDUtil;
import com.example.chichakchessapi.app.playerpreferences.PlayerPreferenceService;
import com.example.chichakchessapi.app.playerpreferences.entities.PlayerPreferenceEntity;
import com.example.chichakchessapi.app.players.entities.PlayerEntity;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.example.chichakchessapi.app.playerspointscalculation.PlayersPointsCalculationService.INITIAL_PLAYER_POINTS;

@Service
public class PlayerService extends BaseService {
    private final MapperUtil mapperUtil;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlayerFindService playerFindService;
    private final PlayerPreferenceService playerPreferenceService;
    private final JWTGenerationService jwtGenerationService;

    public PlayerService(MapperUtil mapperUtil, PlayerRepository playersRepository, PasswordEncoder passwordEncoder, PlayerFindService playerFindService, PlayerPreferenceService playerPreferenceService, JWTGenerationService jwtGenerationService) {
        this.mapperUtil = mapperUtil;
        this.playerRepository = playersRepository;
        this.passwordEncoder = passwordEncoder;
        this.playerFindService = playerFindService;
        this.playerPreferenceService = playerPreferenceService;
        this.jwtGenerationService = jwtGenerationService;
    }

    public PlayerModel createPlayer(RegisterModel registration) {
        PlayerPreferenceEntity playerPreference = playerPreferenceService.createPlayerPreference();
        PlayerEntity playerEntity = mapperUtil.map(registration, PlayerEntity.class);
        playerEntity.setId(UUID.randomUUID().toString());
        playerEntity.setPlayerPreference(playerPreference);
        playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
        playerEntity.setRole(PlayerRole.USER);
        playerEntity.setPoints(INITIAL_PLAYER_POINTS);

        if (playerRepository.findByEmail(playerEntity.getEmail()).isPresent()) {
            throw notSupportedOperation(
                    CustomMessageUtil.PLAYER_ALREADY_EXIST,
                    CustomMessageUtil.PLAYER_EMAIL + playerEntity.getId()
            ).get();
        }

        return mapperUtil.map(playerRepository.save(playerEntity), PlayerModel.class);
    }

    public String getPlayersEncodedPasswordByID(String id) {
        Optional<String> playerID = UUIDUtil.convertFromStringToUUID(id);
        if (playerID.isEmpty()) {
            throw notSupportedOperation(
                    CustomMessageUtil.GENERAL_NOT_VALID_UUID,
                    CustomMessageUtil.GENERAL_PROVIDED_ID + id
            ).get();
        }

        PlayerEntity playerEntity = playerRepository.findById(playerID.get())
                .orElseThrow(
                        notFound(
                                CustomMessageUtil.PLAYER_DOES_NOT_EXIST,
                                CustomMessageUtil.GENERAL_PROVIDED_ID + id
                        )
                );
        return playerEntity.getPassword();
    }

    public void deleteUserByUserByID(String id, String jwtToken) {
        String userEmailFromJWTToken = jwtGenerationService.extractClaims(jwtToken).getSubject();
        PlayerRole role = playerFindService.getPlayerByEmail(userEmailFromJWTToken).getRole();

        String deleteUserEmail = playerFindService.getPlayerByID(id).getEmail();

        if (!userEmailFromJWTToken.equals(deleteUserEmail) && role != PlayerRole.ADMIN) {
            throw unauthorized(
                    CustomMessageUtil.PLAYER_IS_NOT_ADMIN,
                    CustomMessageUtil.PLAYER_EMAIL + userEmailFromJWTToken
            ).get();
        }

        Optional<String> playerID = UUIDUtil.convertFromStringToUUID(id);
        if (playerID.isEmpty()) {
            throw notSupportedOperation(
                    CustomMessageUtil.GENERAL_NOT_VALID_UUID,
                    CustomMessageUtil.GENERAL_PROVIDED_ID + id
            ).get();
        }

        PlayerEntity playerEntity = playerRepository.findById(playerID.get())
                .orElseThrow(
                        notFound(
                                CustomMessageUtil.PLAYER_DOES_NOT_EXIST,
                                CustomMessageUtil.GENERAL_PROVIDED_ID + id
                        )
                );
        playerRepository.delete(playerEntity);
    }
}