package com.example.chichakchessapi.app.matchmaking;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.JWTGenerationService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.games.GameMapper;
import com.example.chichakchessapi.app.games.dtos.GameResponseDTO;
import com.example.chichakchessapi.app.games.models.GameModel;
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
    private final JWTGenerationService jwtGenerationService;

    public MatchmakingQueueService(MatchmakingService matchmakingService, PlayerFindService playerFindService, JWTGenerationService jwtGenerationService) {
        this.jwtGenerationService = jwtGenerationService;
        this.matchmakingQueue = new ConcurrentLinkedDeque<>();
        this.playerIDsToResultsMatchmaking = new ConcurrentHashMap<>();
        this.matchmakingService = matchmakingService;
        this.playerFindService = playerFindService;
    }

    public void enqueuePlayer(String id, String jwtToken, DeferredResult<ResponseEntity<GameResponseDTO>> resultMatchmaking) {
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
        String userEmailFromJWTToken = jwtGenerationService.extractClaims(jwtToken).getSubject();
        String userEmail = playerFindService.getPlayerByID(id).getEmail();

        if (!userEmailFromJWTToken.equals(userEmail)) {
            throw unauthorized(
                    CustomMessageUtil.GAME_CANNOT_ENROLL_GAME_AS_OTHER_PLAYER,
                    CustomMessageUtil.PLAYER_EMAIL + userEmailFromJWTToken
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