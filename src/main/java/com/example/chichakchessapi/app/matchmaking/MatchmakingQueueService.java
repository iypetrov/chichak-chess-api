package com.example.chichakchessapi.app.matchmaking;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.JWTGenerationService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.engine.GameCurrentStateService;
import com.example.chichakchessapi.app.games.GameMapper;
import com.example.chichakchessapi.app.games.dtos.GameResponseDTO;
import com.example.chichakchessapi.app.players.PlayerFindService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class MatchmakingQueueService extends BaseService {
    private final ConcurrentLinkedDeque<PlayerModel> matchmakingQueue;
    private final Map<String, DeferredResult<ResponseEntity<GameResponseDTO>>> playerIDsToResultsMatchmaking;
    private final MatchmakingService matchmakingService;
    private final PlayerFindService playerFindService;
    private final GameCurrentStateService gameCurrentStateService;
    private final JWTGenerationService jwtGenerationService;

    public MatchmakingQueueService(MatchmakingService matchmakingService, PlayerFindService playerFindService, GameCurrentStateService gameCurrentStateService, JWTGenerationService jwtGenerationService) {
        this.matchmakingQueue = new ConcurrentLinkedDeque<>();
        this.playerIDsToResultsMatchmaking = new ConcurrentHashMap<>();
        this.matchmakingService = matchmakingService;
        this.playerFindService = playerFindService;
        this.gameCurrentStateService = gameCurrentStateService;
        this.jwtGenerationService = jwtGenerationService;
    }

    public void enqueuePlayer(String id, String jwtToken, DeferredResult<ResponseEntity<GameResponseDTO>> resultMatchmaking) {
        String userIDFromJWTToken = jwtGenerationService.extractClaims(jwtToken).getSubject();
        PlayerModel player = playerFindService.getPlayerByID(id);

        Optional<String> gameID = gameCurrentStateService.getActiveGameIDOfPlayer(player.getId());
        if (gameID.isPresent()) {
           throw invalidRequest(
                   CustomMessageUtil.PLAYER_IS_ALREADY_IN_GAME,
                   CustomMessageUtil.GAME_ID + gameID.get()
           ).get();
        }

        if (!userIDFromJWTToken.equals(player.getId())) {
            throw unauthorized(
                    CustomMessageUtil.GAME_CANNOT_ENROLL_GAME_AS_OTHER_PLAYER,
                    CustomMessageUtil.PLAYER_ID + userIDFromJWTToken
            ).get();
        }

        PlayerModel playerModel = playerFindService.getPlayerByID(id);
        matchmakingQueue.add(playerModel);
        playerIDsToResultsMatchmaking.put(id, resultMatchmaking);

        if (matchmakingQueue.size() >= 2) {
            PlayerModel playerOne = matchmakingQueue.pollFirst();
            PlayerModel playerTwo = matchmakingQueue.pollFirst();

            GameResponseDTO game = GameMapper.convertGameModelToGameResponseDTO(
                    matchmakingService.createMatch(playerOne, playerTwo)
            );
            playerIDsToResultsMatchmaking.get(playerOne.getId()).setResult(ResponseEntity.status(HttpStatus.CREATED).body(game));
            playerIDsToResultsMatchmaking.get(playerTwo.getId()).setResult(ResponseEntity.status(HttpStatus.CREATED).body(game));

            playerIDsToResultsMatchmaking.remove(playerOne.getId());
            playerIDsToResultsMatchmaking.remove(playerTwo.getId());
        }
    }

    public void removePlayerFromMatchmaking(String id, String jwtToken) {
        String userIDFromJWTToken = jwtGenerationService.extractClaims(jwtToken).getSubject();
        String userID = playerFindService.getPlayerByID(id).getId();

        if (!userIDFromJWTToken.equals(userID)) {
            throw unauthorized(
                    CustomMessageUtil.GAME_CANNOT_ENROLL_GAME_AS_OTHER_PLAYER,
                    CustomMessageUtil.PLAYER_ID + userIDFromJWTToken
            ).get();
        }

        removePlayerFromQueue(id);
    }

    public void removePlayerFromQueue(String id) {
        Optional<PlayerModel> player = matchmakingQueue.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (player.isPresent()) {
            matchmakingQueue.remove(player.get());
            playerIDsToResultsMatchmaking.remove(id);
        }
    }
}