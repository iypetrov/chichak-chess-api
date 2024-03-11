package com.example.chichakchessapi.app.gamestates;

import com.example.chichakchessapi.app.BaseService;
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

import static com.example.chichakchessapi.app.engine.ChessStateValidatorService.INIT_BOARD_POSITION;

@Service
public class GameStateService extends BaseService {
    private final GameStateRepository gameStateRepository;
    private final GameStateSpecification gameStateSpecification;

    public GameStateService(GameStateRepository gameStateRepository, GameStateSpecification gameStateSpecification) {
        this.gameStateRepository = gameStateRepository;
        this.gameStateSpecification = gameStateSpecification;
    }

    public void createInitGameState(GameModel game) {
        GameStateModel gameState = new GameStateModel(
                UUID.randomUUID().toString(),
                game,
                INIT_BOARD_POSITION,
                PieceColor.WHITE,
                true,
                0,
                false,
                Timestamp.from(Instant.now())
        );

        gameStateRepository.save(
                map(gameState, GameStateEntity.class)
        );
    }

    public List<GameStateModel> getGameStatesByGameID(String gameID) {
        Specification<GameStateEntity> spec = Specification
                .where(gameStateSpecification.gameEquals(gameID));
        List<GameStateEntity> gameStates = gameStateRepository.findAll(spec);
        return map(gameStates, GameStateModel.class);
    }
}