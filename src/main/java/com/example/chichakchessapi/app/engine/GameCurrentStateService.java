package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import com.github.bhlangonijr.chesslib.Piece;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class GameCurrentStateService extends BaseService {
    // In a real implementation I would use local/distributed cache
    // For the distributed cache, like Redis, I'd use it to store only the most recent data instead of directly writing to it
    // When the server starts, it would fetch the newest data from the distributed cache
    // Any new events would be recorded in the local cache first, and then periodically, the latest records would be saved to the distributed cache
    private final Map<String, GameStateModel> gameIDsToLatestGameStates;
    //The value contains two values - first is game id, second is player color in this game
    private final Map<String, List<String>> playerIDsToGameIDs;
    private final static int POSITION_GAME_ID_IN_PLAYER_IDS_TO_GAME_IDS = 0;
    private final static int POSITION_PLAYER_COLOR_IN_PLAYER_IDS_TO_GAME_IDS = 1;
    private final Map<String, List<String>> gameIDToPlayerIDs;

    @Scheduled(cron = "0 * * ? * *")
    void logNumberOfPersistedGameStatesLastMinute() {
        log.info(String.format("%d active games in the last minute", gameIDsToLatestGameStates.keySet().size()));
    }

    public GameCurrentStateService() {
        this.gameIDsToLatestGameStates = new ConcurrentHashMap<>();
        this.playerIDsToGameIDs = new ConcurrentHashMap<>();
        this.gameIDToPlayerIDs = new ConcurrentHashMap<>();
    }

    public void addLatestStateToGame(String gameID, GameStateModel state) {
        gameIDsToLatestGameStates.put(gameID, state);
    }

    public Optional<GameStateModel> getLatestGameStateByGameID(String gameID) {
        if (Objects.isNull(gameID)) {
            return Optional.empty();
        }

        return Optional.ofNullable(gameIDsToLatestGameStates.get(gameID));
    }

    public void addPlayerToInGamePlayersCache(String playerID, String gameID, PieceColor color) {
        playerIDsToGameIDs.put(playerID, List.of(gameID, color.toString()));
    }

    public Optional<String> getActiveGameIDOfPlayer(String playerID) {
        if (!playerIDsToGameIDs.containsKey(playerID)) {
            return Optional.empty();
        }

        return Optional.of(playerIDsToGameIDs.get(playerID).get(POSITION_GAME_ID_IN_PLAYER_IDS_TO_GAME_IDS));
    }

    public Optional<String> getPlayerColorOfActiveGame(String playerID) {
        if (!playerIDsToGameIDs.containsKey(playerID)) {
            return Optional.empty();
        }

        return Optional.of(playerIDsToGameIDs.get(playerID).get(POSITION_PLAYER_COLOR_IN_PLAYER_IDS_TO_GAME_IDS));
    }

    public void addGameParticipants(String gameID, String playerOneID, String playerTwoID) {
       gameIDToPlayerIDs.put(gameID, List.of(playerOneID, playerTwoID));

    }

    public List<String> getGameParticipantIDsFromGame(String gameID) {
        if (!gameIDToPlayerIDs.containsKey(gameID)) {
           return List.of();
        }

        return gameIDToPlayerIDs.get(gameID);
    }

    public void removeGameInfoFromCache(String playerID, String gameID) {
        playerIDsToGameIDs.remove(playerID);
        gameIDsToLatestGameStates.remove(gameID);
        gameIDToPlayerIDs.remove(gameID);
    }
}