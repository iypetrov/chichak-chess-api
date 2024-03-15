package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.JWTGenerationService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.engine.models.GameMovementModel;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import com.github.bhlangonijr.chesslib.Board;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChessMovementService extends BaseService {
    private final GameCurrentStateService gameCurrentStateService;
    private final GamePersistenceBatchQueueService gamePersistenceBatchQueueService;
    private final JWTGenerationService jwtGenerationService;

    public ChessMovementService(GameCurrentStateService gameCurrentStateService, GamePersistenceBatchQueueService gamePersistenceBatchQueueService, JWTGenerationService jwtGenerationService) {
        this.gameCurrentStateService = gameCurrentStateService;
        this.gamePersistenceBatchQueueService = gamePersistenceBatchQueueService;
        this.jwtGenerationService = jwtGenerationService;
    }


    public GameStateModel addPlayerMovement(GameMovementModel gameMovement, String jwtToken) {
        String userIDFromJWTToken = jwtGenerationService.extractClaims(jwtToken).getSubject();
        if (!gameMovement.getPlayerID().equals(userIDFromJWTToken)) {
            throw unauthorized(
                    CustomMessageUtil.PLAYER_IS_NOT_IN_GAME,
                    CustomMessageUtil.PLAYER_ID + gameMovement.getPlayerID()
            ).get();
        }

        return executeMovement(gameMovement);
    }

    private GameStateModel executeMovement(GameMovementModel gameMovement) {
        GameStateModel gameState = gameCurrentStateService.getLatestGameStateByGameID(gameMovement.getGameID());
        if (Objects.isNull(gameState)) {
            throw notFound(
                    CustomMessageUtil.GAME_DOES_NOT_EXIST,
                    CustomMessageUtil.GAME_ID + gameMovement.getGameID()
            ).get();
        }

        Optional<String> activeGameIDOfPlayer = gameCurrentStateService.getActiveGameIDOfPlayer(gameMovement.getPlayerID());
        if (activeGameIDOfPlayer.isEmpty() || !activeGameIDOfPlayer.get().equals(gameMovement.getPlayerID())
        ) {
            throw notFound(
                    CustomMessageUtil.PLAYER_IS_NOT_IN_GAME,
                    CustomMessageUtil.PLAYER_ID + gameMovement.getPlayerID()
            ).get();
        }

        // TODO: Add description what would change here in real implementation
        Board board = new Board();
        board.loadFromFen(gameState.toString());

        if (!board.doMove(gameMovement.getMovement())) {
            return gameState;
        }

        if (board.isMated()) {
            // TODO: Make the teardown operations for end of the game
            return gameState;
        }

        GameStateModel newGameState = new GameStateModel(
                board.getFen(),
                UUID.randomUUID().toString(),
                gameState.getGame(),
                false,
                Timestamp.from(Instant.now())
        );

        gameCurrentStateService.addLatestStateToGame(gameMovement.getGameID(), newGameState);
        gameCurrentStateService.addPlayerToInGamePlayersCache(gameMovement.getPlayerID(), gameMovement.getGameID());
        gamePersistenceBatchQueueService.addElementToPersistentQueue(newGameState);

        return gameState;
    }

    public void surrenderPlayer(String playerID) {

    }

    private void teardownAfterGameEnds(String gameID, String winnerID, String loserID) {

    }
}