package com.example.chichakchessapi.app.matchmaking;

import com.example.chichakchessapi.app.engine.GameCurrentStateService;
import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.gameparticipants.GameParticipantService;
import com.example.chichakchessapi.app.games.GameService;
import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.gamestates.GameStateService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchmakingService {
    private final GameService gameService;
    private final GameParticipantService gameParticipantService;
    private final GameStateService gameStateService;
    private final GameCurrentStateService gameCurrentStateService;

    public MatchmakingService(GameService gameService, GameParticipantService gameParticipantService, GameStateService gameStateService, GameCurrentStateService gameCurrentStateService) {
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
        gameStateService.createInitGameState(game);
        gameCurrentStateService.addGameParticipants(game.getId(), playerOne.getId(), playerTwo.getId());
        return game;
    }
}