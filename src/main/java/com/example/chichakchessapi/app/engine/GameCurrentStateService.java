package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameCurrentStateService extends BaseService {
    private final Map<String, GameStateModel> gameIDsToLatestGameStates;
    private final Map<String, String> playerIDsToGameIDs;

    public GameCurrentStateService() {
        this.gameIDsToLatestGameStates = new ConcurrentHashMap<>();
        this.playerIDsToGameIDs = new ConcurrentHashMap<>();
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

    public void removeGameInfoFromCache(String playerID, String gameID) {
        playerIDsToGameIDs.remove(playerID);
        gameIDsToLatestGameStates.remove(gameID);
    }
}