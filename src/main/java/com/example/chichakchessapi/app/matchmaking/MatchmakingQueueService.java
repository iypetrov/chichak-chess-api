package com.example.chichakchessapi.app.matchmaking;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.JWTGenerationService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.players.PlayerFindService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class MatchmakingQueueService extends BaseService {
    private final ConcurrentLinkedDeque<PlayerModel> matchmakingQueue;
    private final Map<String, DeferredResult<GameModel>> playerIDsToResultsMatchmaking;
    private final MatchmakingService matchmakingService;
    private final PlayerFindService playerFindService;
    private final JWTGenerationService jwtGenerationService;

    public MatchmakingQueueService(MatchmakingService matchmakingService, PlayerFindService playerFindService, JWTGenerationService jwtGenerationService) {
        this.jwtGenerationService = jwtGenerationService;
        this.matchmakingQueue = new ConcurrentLinkedDeque<>();
        this.playerIDsToResultsMatchmaking = new ConcurrentHashMap<>();
        this.matchmakingService = matchmakingService;
        this.playerFindService = playerFindService;
    }

    public void enqueuePlayer(String id, String jwtToken, DeferredResult<GameModel> resultMatchmaking) {
        String userEmailFromJWTToken = jwtGenerationService.extractClaims(jwtToken).getSubject();
        String userEmail = playerFindService.getPlayerByID(id).getEmail();

        if (!userEmailFromJWTToken.equals(userEmail)) {
            throw unauthorized(
                    CustomMessageUtil.GAME_CANNOT_ENROLL_GAME_AS_OTHER_PLAYER,
                    CustomMessageUtil.PLAYER_EMAIL + userEmailFromJWTToken
            ).get();
        }

        PlayerModel playerModel = playerFindService.getPlayerByID(id);
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