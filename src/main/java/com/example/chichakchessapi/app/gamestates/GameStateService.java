package com.example.chichakchessapi.app.gamestates;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.common.MapperUtil;
import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.gamestates.entities.GameStateEntity;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import java.util.List;

@Service
public class GameStateService extends BaseService {
    private final MapperUtil mapperUtil;
    private final GameStateRepository gameStateRepository;
    private final GameStateSpecification gameStateSpecification;

    public GameStateService(MapperUtil mapperUtil, GameStateRepository gameStateRepository, GameStateSpecification gameStateSpecification) {
        this.mapperUtil = mapperUtil;
        this.gameStateRepository = gameStateRepository;
        this.gameStateSpecification = gameStateSpecification;
    }

    public void createInitGameState(GameModel game) {
        GameStateModel gameState = new GameStateModel(
                UUID.randomUUID().toString(),
                game,
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",
                PieceColor.WHITE,
                "KQkq",
                "-",
                0,
                1,
                false,
                Timestamp.from(Instant.now())
        );

        gameStateRepository.save(
                mapperUtil.map(gameState, GameStateEntity.class)
        );
    }

    public List<GameStateModel> getGameStatesByGameID(String gameID) {
        Specification<GameStateEntity> spec = Specification
                .where(gameStateSpecification.gameEquals(gameID));
        List<GameStateEntity> gameStates = gameStateRepository.findAll(spec);
        return mapperUtil.map(gameStates, GameStateModel.class);
    }
}