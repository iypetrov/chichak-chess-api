package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.JWTGenerationService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.engine.models.GameMovementModel;
import com.example.chichakchessapi.app.gameparticipants.GameParticipantService;
import com.example.chichakchessapi.app.gameparticipants.models.GameParticipantModel;
import com.example.chichakchessapi.app.games.GameService;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import com.example.chichakchessapi.app.players.PlayerFindService;
import com.example.chichakchessapi.app.players.PlayerService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import com.example.chichakchessapi.app.playerspointscalculation.PlayersPointsCalculationService;
import com.github.bhlangonijr.chesslib.Board;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class ChessMovementService extends BaseService {
    private final GameCurrentStateService gameCurrentStateService;
    private final GamePersistenceBatchQueueService gamePersistenceBatchQueueService;
    private final GameParticipantService gameParticipantService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final PlayerFindService playerFindService;
    private final PlayersPointsCalculationService playersPointsCalculationService;
    private final JWTGenerationService jwtGenerationService;

    public ChessMovementService(
            GameCurrentStateService gameCurrentStateService,
            GamePersistenceBatchQueueService gamePersistenceBatchQueueService,
            GameParticipantService gameParticipantService,
            GameService gameService,
            PlayerService playerService, PlayerFindService playerFindService,
            PlayersPointsCalculationService playersPointsCalculationService,
            JWTGenerationService jwtGenerationService
    ) {
        this.gameCurrentStateService = gameCurrentStateService;
        this.gamePersistenceBatchQueueService = gamePersistenceBatchQueueService;
        this.gameParticipantService = gameParticipantService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.playerFindService = playerFindService;
        this.playersPointsCalculationService = playersPointsCalculationService;
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

        GameStateModel newGameState = new GameStateModel(
                board.getFen(),
                UUID.randomUUID().toString(),
                gameState.getGame(),
                false,
                Timestamp.from(Instant.now())
        );

        if (board.isMated()) {
            String winnerID = gameMovement.getPlayerID();
            Optional<String> loserID = gameCurrentStateService.getGameParticipantIDsFromGame(
                            gameMovement.getGameID()
                    ).stream()
                    .filter(p -> !winnerID.equals(p))
                    .findFirst();
            loserID.ifPresent(s -> teardownAfterGameEnds(newGameState, winnerID, s, false));
        } else if (board.isDraw()) {
            List<String> participants = gameCurrentStateService.getGameParticipantIDsFromGame(gameMovement.getGameID());
            teardownAfterGameEnds(newGameState, participants.get(0), participants.get(1), true);
        } else {
            gameCurrentStateService.addLatestStateToGame(gameMovement.getGameID(), newGameState);
            gameCurrentStateService.addPlayerToInGamePlayersCache(gameMovement.getPlayerID(), gameMovement.getGameID());
        }

        gamePersistenceBatchQueueService.addElementToPersistentQueue(newGameState);

        return gameState;
    }

    public void surrenderPlayer(String loserID, String jwtToken) {
        String userIDFromJWTToken = jwtGenerationService.extractClaims(jwtToken).getSubject();
        PlayerModel loser = playerFindService.getPlayerByID(userIDFromJWTToken);
        if (!loser.getId().equals(userIDFromJWTToken)) {
            throw unauthorized(
                    CustomMessageUtil.PLAYER_IS_NOT_IN_GAME,
                    CustomMessageUtil.PLAYER_ID + loser.getId()
            ).get();
        }

        Optional<String> gameID = gameCurrentStateService.getActiveGameIDOfPlayer(loserID);
        if (gameID.isPresent()) {
            GameStateModel latestGameState = gameCurrentStateService.getLatestGameStateByGameID(gameID.get());
            List<String> participants = gameCurrentStateService.getGameParticipantIDsFromGame(gameID.get());
            if (!participants.contains(loserID)) {
                throw notFound(
                        CustomMessageUtil.PLAYER_IS_NOT_IN_GAME,
                        CustomMessageUtil.PLAYER_ID + loserID
                ).get();
            }

            Optional<String> winnerID = participants.stream()
                    .filter(p -> !p.equals(loserID))
                    .findFirst();
            if (winnerID.isEmpty()) {
                throw notFound(
                        CustomMessageUtil.PLAYER_ALONE_IN_GAME,
                        CustomMessageUtil.PLAYER_ID + loserID
                ).get();
            }
            latestGameState.setIsFinal(true);
            teardownAfterGameEnds(latestGameState, winnerID.get(), loserID, false);
        }
    }

    private void teardownAfterGameEnds(GameStateModel gameState, String winnerID, String loserID, boolean isDraw) {
        // clear cache & send the final state
        gameState.setIsFinal(true);
        gameCurrentStateService.removeGameInfoFromCache(
                winnerID,
                gameState.getGame().getId()
        );
        gameCurrentStateService.removeGameInfoFromCache(
                loserID,
                gameState.getGame().getId()
        );

        // save the result for winner/loser
        List<GameParticipantModel> gameParticipants = gameParticipantService.getAllGameParticipantsByGameID(
                gameState.getGame().getId()
        );
        setResultEndGameForPlayer(gameParticipants, winnerID, true, isDraw);
        setResultEndGameForPlayer(gameParticipants, loserID, false, isDraw);
        gameParticipantService.updateMultipleGameParticipants(gameParticipants);

        // save the result for game length
        gameService.setFinalStatusOfGame(gameState.getGame(), gameState.getFullmoveNumber());

        // update player points after game
        playerService.updateMultiplePlayers(
                playersPointsCalculationService.updatePointsPlayersAfterGame(winnerID, loserID, isDraw)
        );
    }

    private void setResultEndGameForPlayer(
            List<GameParticipantModel> gameParticipants,
            String playerID,
            boolean isWinner,
            boolean isDraw
    ) {
        gameParticipants.stream()
                .filter(gp -> gp.getPlayer().getId().equals(playerID))
                .findFirst()
                .ifPresent(playerState -> {
                            if (isDraw) {
                                playerState.setIsWinner(isWinner);
                            } else {
                                playerState.setIsWinner(false);
                            }

                            playerState.setIsDraw(isDraw);
                        }
                );
    }
}