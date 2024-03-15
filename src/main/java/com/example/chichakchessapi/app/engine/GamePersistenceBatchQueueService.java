package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePersistenceBatchQueueService extends BaseService {
    private final List<GameStateModel> gameStatesToPersistQueue;

    public GamePersistenceBatchQueueService(List<GameStateModel> gameStatesToPersistQueue) {
        this.gameStatesToPersistQueue = gameStatesToPersistQueue;
    }

    public void addElementToPersistantQueue(GameStateModel gameState) {
       gameStatesToPersistQueue.add(gameState);
    }
}