package com.example.chichakchessapi.app.matchmaking;

import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.players.PlayerService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class MatchmakingQueueService {
    private final ConcurrentLinkedDeque<PlayerModel> matchmakingQueue;
    private final Map<String, DeferredResult<GameModel>> playerIDsToResultsMatchmaking;
    private final MatchmakingService matchmakingService;
    private final PlayerService playerService;

    public MatchmakingQueueService(MatchmakingService matchmakingService, PlayerService playerService) {
        this.matchmakingQueue = new ConcurrentLinkedDeque<>();
        this.playerIDsToResultsMatchmaking = new ConcurrentHashMap<>();
        this.matchmakingService = matchmakingService;
        this.playerService = playerService;
    }

    public void enqueuePlayer(String id, DeferredResult<GameModel> resultMatchmaking) {
        PlayerModel playerModel = playerService.getPlayerByID(id);
        matchmakingQueue.add(playerModel);
        playerIDsToResultsMatchmaking.put(id, resultMatchmaking);

        if (matchmakingQueue.size() >= 2) {
            PlayerModel playerOne = matchmakingQueue.pollFirst();
            PlayerModel playerTwo = matchmakingQueue.pollFirst();

            GameModel newGame = matchmakingService.createMatch(playerOne, playerTwo);
            playerIDsToResultsMatchmaking.get(playerOne.getId()).setResult(newGame);
            playerIDsToResultsMatchmaking.get(playerTwo.getId()).setResult(newGame);

            playerIDsToResultsMatchmaking.remove(playerOne.getId());
            playerIDsToResultsMatchmaking.remove(playerTwo.getId());
        }
    }
}