package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChessStateValidatorService {
    public static final String INIT_BOARD_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    private final List<GameStateModel> gameStatesToPersistQueue;
    private final Map<String, GameStateModel> gameStateIdsToLatestGameStates;

    public ChessStateValidatorService() {
        this.gameStatesToPersistQueue = new ArrayList<>();
        this.gameStateIdsToLatestGameStates = new ConcurrentHashMap<>();
    }
}