package com.example.chichakchessapi.app.games;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.common.MapperUtil;
import com.example.chichakchessapi.app.games.entities.GameEntity;
import com.example.chichakchessapi.app.games.models.GameModel;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
public class GameService extends BaseService {
    private final MapperUtil mapperUtil;
    private final GameRepository gameRepository;

    public GameService(MapperUtil mapperUtil, GameRepository gameRepository) {
        this.mapperUtil = mapperUtil;
        this.gameRepository = gameRepository;
    }

    public GameModel createGame() {
        GameEntity gameEntity = new GameEntity(
                UUID.randomUUID().toString(),
                0,
                Timestamp.from(Instant.now()),
                null
        );

        return mapperUtil.map(gameRepository.save(gameEntity), GameModel.class);
    }

    public GameModel setFinalStatusOfGame(
            GameModel game,
            Integer finalFullmoveNumber
    ) {
        GameEntity gameEntity = new GameEntity(
                game.getId(),
                finalFullmoveNumber,
                game.getCreatedOn(),
                Timestamp.from(Instant.now())
        );

        return mapperUtil.map(gameRepository.save(gameEntity), GameModel.class);
    }
}