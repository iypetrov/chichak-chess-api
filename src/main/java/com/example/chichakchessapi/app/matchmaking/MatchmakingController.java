package com.example.chichakchessapi.app.matchmaking;

import com.example.chichakchessapi.app.games.GameMapper;
import com.example.chichakchessapi.app.games.dtos.GameResponseDTO;
import com.example.chichakchessapi.app.games.models.GameModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

import static com.example.chichakchessapi.app.auth.AuthService.COOKIE_AUTH_TOKEN_NAME;

@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController {
    private final MatchmakingQueueService matchmakingQueueService;

    public MatchmakingController(MatchmakingQueueService matchmakingQueueService) {
        this.matchmakingQueueService = matchmakingQueueService;
    }

    @PostMapping("/enroll/{id}")
    public DeferredResult<ResponseEntity<GameResponseDTO>> enrollPlayer(
            @PathVariable("id") String id,
            @CookieValue(name = COOKIE_AUTH_TOKEN_NAME, required = false) String jwtToken
    ) {
        DeferredResult<GameModel> resultMatchmaking = new DeferredResult<>();
        ForkJoinPool.commonPool().submit(() -> matchmakingQueueService.enqueuePlayer(id, jwtToken, resultMatchmaking));

        DeferredResult<ResponseEntity<GameResponseDTO>> response = new DeferredResult<>();
        resultMatchmaking.onCompletion(() -> {
            GameModel game = (GameModel) resultMatchmaking.getResult();
            ResponseEntity<GameResponseDTO> entity = ResponseEntity.ok(
                    GameMapper.convertGameModelToGameResponseDTO(
                            Objects.requireNonNull(game)
                    )
            );
            response.setResult(entity);
        });

        return response;
    }
}