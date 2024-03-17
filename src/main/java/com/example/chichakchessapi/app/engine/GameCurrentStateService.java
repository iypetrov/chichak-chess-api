package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// TODO: Add description what would change here in real implementation
@Service
public class GameCurrentStateService extends BaseService {
    private final Map<String, GameStateModel> gameIDsToLatestGameStates;
    private final Map<String, String> playerIDsToGameIDs;
    private final Map<String, List<String>> gameIDToPlayerIDs;

    public GameCurrentStateService() {
        this.gameIDsToLatestGameStates = new ConcurrentHashMap<>();
        this.playerIDsToGameIDs = new ConcurrentHashMap<>();
        this.gameIDToPlayerIDs = new ConcurrentHashMap<>();
    }

    public void addLatestStateToGame(String gameID, GameStateModel state) {
        gameIDsToLatestGameStates.put(gameID, state);
    }

    public GameStateModel getLatestGameStateByGameID(String gameID) {
        return gameIDsToLatestGameStates.get(gameID);
    }

    public void addPlayerToInGamePlayersCache(String playerID, String gameID) {
        playerIDsToGameIDs.put(playerID, gameID);
    }

    public Optional<String> getActiveGameIDOfPlayer(String playerID) {
        if (!playerIDsToGameIDs.containsKey(playerID)) {
            return Optional.empty();
        }

        return Optional.of(playerIDsToGameIDs.get(playerID));
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