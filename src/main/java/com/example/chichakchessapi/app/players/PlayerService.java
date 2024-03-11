package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.PlayerRole;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.common.UUIDUtil;
import com.example.chichakchessapi.app.players.entities.PlayerEntity;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.chichakchessapi.app.playerspointscalculation.PlayersPointsCalculationService.INITIAL_PLAYER_POINTS;

@Service
public class PlayerService extends BaseService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public PlayerService(PlayerRepository playersRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PlayerModel createPlayer(RegisterModel registration) {
        PlayerEntity playerEntity = map(registration, PlayerEntity.class);
        playerEntity.setId(UUID.randomUUID().toString());
        playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
        playerEntity.setRole(PlayerRole.USER);
        playerEntity.setPoints(INITIAL_PLAYER_POINTS);

        if (playerRepository.findByEmail(playerEntity.getEmail()).isPresent()) {
            throw notSupportedOperation(
                    CustomMessageUtil.PLAYER_ALREADY_EXIST,
                    CustomMessageUtil.PLAYER_EMAIL + playerEntity.getId()
            ).get();
        }

        return map(playerRepository.save(playerEntity), PlayerModel.class);
    }

    public PlayerModel getPlayerByID(String id) {
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
        return map(playerEntity, PlayerModel.class);
    }

    public PlayerModel getPlayerByEmail(String email) {
        Optional<PlayerEntity> playerEntity = playerRepository.findByEmail(email);
        if (playerEntity.isEmpty()) {
            throw invalidRequest(
                    CustomMessageUtil.PLAYER_DOES_NOT_EXIST,
                    CustomMessageUtil.PLAYER_EMAIL + email
            ).get();
        }

        return map(playerEntity, PlayerModel.class);
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

    public List<PlayerModel> getAllPlayers() {
        List<PlayerEntity> playerEntities = playerRepository.findAll();
        return map(playerEntities, PlayerModel.class);
    }
}