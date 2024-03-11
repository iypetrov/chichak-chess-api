package com.example.chichakchessapi.app.games;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.games.entities.GameEntity;
import com.example.chichakchessapi.app.games.models.GameModel;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
public class GameService extends BaseService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameModel createGame() {
        GameEntity gameEntity = new GameEntity(
                UUID.randomUUID().toString(),
                0,
                Timestamp.from(Instant.now()),
                null
        );

        return map(gameRepository.save(gameEntity), GameModel.class);
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

        return map(gameRepository.save(gameEntity), GameModel.class);
    }
}