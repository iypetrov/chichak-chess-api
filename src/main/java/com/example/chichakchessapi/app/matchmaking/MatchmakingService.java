package com.example.chichakchessapi.app.matchmaking;

import com.example.chichakchessapi.app.common.MapperUtil;
import com.example.chichakchessapi.app.engine.GameCurrentStateService;
import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.gameparticipants.GameParticipantService;
import com.example.chichakchessapi.app.games.GameService;
import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.gamestates.GameStateService;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MatchmakingService {
    private final MapperUtil mapperUtil;
    private final GameService gameService;
    private final GameParticipantService gameParticipantService;
    private final GameStateService gameStateService;
    private final GameCurrentStateService gameCurrentStateService;

    public MatchmakingService(MapperUtil mapperUtil, GameService gameService, GameParticipantService gameParticipantService, GameStateService gameStateService, GameCurrentStateService gameCurrentStateService) {
        this.mapperUtil = mapperUtil;
        this.gameService = gameService;
        this.gameParticipantService = gameParticipantService;
        this.gameStateService = gameStateService;
        this.gameCurrentStateService = gameCurrentStateService;
    }

    @Transactional
    public GameModel createMatch(PlayerModel playerOne, PlayerModel playerTwo) {
        GameModel game = gameService.createGame();
        gameParticipantService.createGameParticipant(game, playerOne, playerTwo, PieceColor.WHITE);
        gameParticipantService.createGameParticipant(game, playerTwo, playerOne, PieceColor.BLACK);
        GameStateModel gameState = gameStateService.createInitGameState(game);
        gameCurrentStateService.addLatestStateToGame(
                game.getId(),
                mapperUtil.map(gameState, GameStateModel.class)
        );
        log.info(gameCurrentStateService.getLatestGameStateByGameID(game.getId()).getId());
        gameCurrentStateService.addGameParticipants(game.getId(), playerOne.getId(), playerTwo.getId());
        gameCurrentStateService.addPlayerToInGamePlayersCache(playerOne.getId(), game.getId());
        gameCurrentStateService.addPlayerToInGamePlayersCache(playerTwo.getId(), game.getId());
        return game;
    }
}