package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.common.CustomMessageUtilService;
import com.example.chichakchessapi.app.common.UUIDUtilService;
import com.example.chichakchessapi.app.players.dtos.PlayerResponseDTO;
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

    public PlayerResponseDTO convertPlayerModelToPlayerResponseDTO(PlayerModel playerModel) {
        return map(playerModel, PlayerResponseDTO.class);
    }

    public List<PlayerResponseDTO> convertPlayerModelsToPlayerResponseDTOs(List<PlayerModel> playerModels) {
        return map(playerModels, PlayerResponseDTO.class);
    }

    public PlayerModel createPlayer(RegisterModel registerModel) {
        PlayerEntity playerEntity = map(registerModel, PlayerEntity.class);
        playerEntity.setId(UUID.randomUUID().toString());
        playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
        playerEntity.setPoints(INITIAL_PLAYER_POINTS);

        if (playerRepository.findByEmail(playerEntity.getEmail()).isPresent()) {
            throw notSupportedOperation(
                    CustomMessageUtilService.PLAYER_ALREADY_EXIST,
                    CustomMessageUtilService.PLAYER_EMAIL + playerEntity.getId()
            ).get();
        }

        return map(playerRepository.save(playerEntity), PlayerModel.class);
    }

    public PlayerModel getPlayerByID(String id) {
        Optional<String> playerID = UUIDUtilService.convertFromStringToUUID(id);
        if (playerID.isEmpty()) {
            throw notSupportedOperation(
                    CustomMessageUtilService.GENERAL_NOT_VALID_UUID,
                    CustomMessageUtilService.GENERAL_PROVIDED_ID + id
            ).get();
        }

        PlayerEntity playerEntity = playerRepository.findById(playerID.get())
                .orElseThrow(
                        notFound(
                                CustomMessageUtilService.PLAYER_DOES_NOT_EXIST,
                                CustomMessageUtilService.GENERAL_PROVIDED_ID + id
                        )
                );
        return map(playerEntity, PlayerModel.class);
    }

    public PlayerModel getPlayerByEmail(String email) {
        Optional<PlayerEntity> playerEntity = playerRepository.findByEmail(email);
        if (playerEntity.isEmpty()) {
            throw invalidRequest(
                    CustomMessageUtilService.PLAYER_DOES_NOT_EXIST,
                    CustomMessageUtilService.PLAYER_EMAIL + email
            ).get();
        }

        return map(playerEntity, PlayerModel.class);
    }

    public List<PlayerModel> getAllPlayers() {
        List<PlayerEntity> playerEntities = playerRepository.findAll();
        return map(playerEntities, PlayerModel.class);
    }
}