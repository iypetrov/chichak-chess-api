package com.example.chichakchessapi.app.gamestates;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.common.MapperUtil;
import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.gamestates.entities.GameStateEntity;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Queue;
import java.util.UUID;

import java.util.List;

@Slf4j
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

    @Transactional
    public void persistMultipleGameStatesInBatches(Queue<GameStateModel> states) {
        try {
            gameStateRepository.saveAll(
                    mapperUtil.map(List.of(states), GameStateEntity.class)
            );
        } catch (RuntimeException ex) {
            log.error(
                    CustomMessageUtil.GENERAL_PERSISTENCE_OPERATION_FAILED,
                    ex.getMessage()
            );
        }
    }

    public GameStateModel getGameStatesByGameID(String gameID) {
        Specification<GameStateEntity> spec = Specification
                .where(gameStateSpecification.gameEquals(gameID));
        List<GameStateEntity> gameStates = gameStateRepository.findAll(spec);
        return mapperUtil.map(gameStates.get(0), GameStateModel.class);
    }
}