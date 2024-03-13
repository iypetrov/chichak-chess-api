package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.common.MapperUtil;
import com.example.chichakchessapi.app.common.UUIDUtil;
import com.example.chichakchessapi.app.players.entities.PlayerEntity;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerFindService extends BaseService {
    private final MapperUtil mapperUtil;
    private final PlayerRepository playerRepository;

    public PlayerFindService(MapperUtil mapperUtil, PlayerRepository playerRepository) {
        this.mapperUtil = mapperUtil;
        this.playerRepository = playerRepository;
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
        return mapperUtil.map(playerEntity, PlayerModel.class);
    }

    public PlayerModel getPlayerByEmail(String email) {
        Optional<PlayerEntity> playerEntity = playerRepository.findByEmail(email);
        if (playerEntity.isEmpty()) {
            throw invalidRequest(
                    CustomMessageUtil.PLAYER_DOES_NOT_EXIST,
                    CustomMessageUtil.PLAYER_EMAIL + email
            ).get();
        }

        return mapperUtil.map(playerEntity, PlayerModel.class);
    }

    public List<PlayerModel> getAllPlayers() {
        List<PlayerEntity> playerEntities = playerRepository.findAll();
        return mapperUtil.map(playerEntities, PlayerModel.class);
    }
}
