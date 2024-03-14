package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import com.github.bhlangonijr.chesslib.Board;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChessStateValidatorService {
    public static final String INIT_BOARD_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final List<GameStateModel> gameStatesToPersistQueue;
    private final Map<String, GameStateModel> gameStateIDsToLatestGameStates;
    private final Map<String, String> playerIDsToGameIDs;

    public ChessStateValidatorService() {
        this.gameStatesToPersistQueue = new ArrayList<>();
        this.gameStateIDsToLatestGameStates = new ConcurrentHashMap<>();
        this.playerIDsToGameIDs = new ConcurrentHashMap<>();
    }

    public boolean makeMove(String gameID, String playerID, String move) {
        if (playerIDsToGameIDs.get(playerID) == null || !playerIDsToGameIDs.get(playerID).equals(gameID)) {
            return false;
        }

        GameStateModel gameState = gameStateIDsToLatestGameStates.get(gameID);
        if (gameState == null) {
            return false;
        }

        Board board = new Board();
        board.loadFromFen(gameState.toString());

        boolean isMoveSuccessful = board.doMove(move);
        if (isMoveSuccessful) {
           GameStateModel newGameState = new GameStateModel(board.getFen());
           gameStateIDsToLatestGameStates.put(gameID, newGameState);
           gameStatesToPersistQueue.add(newGameState);
        }

        return isMoveSuccessful;
    }
}