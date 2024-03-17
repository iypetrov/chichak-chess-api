package com.example.chichakchessapi.app.engine;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.gamestates.GameStateService;
import com.example.chichakchessapi.app.gamestates.models.GameStateModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class GamePersistenceBatchQueueService extends BaseService {
    private static final int DEFAULT_RETRY_COUNT_PERSISTING_THE_RESULT = 2;
    private static final long DEFAULT_INTERVAL_TO_PERSIST_THE_RESULT = 1107L;
    private final AtomicInteger counterPersistedGameStatesLastMinute = new AtomicInteger(0);
    private boolean isFirstQueueCurrent = true;

    private final Queue<GameStateModel> gameStatesToPersistQueueFirst;
    private final Queue<GameStateModel> gameStatesToPersistQueueSecond;

    private final GameStateService gameStateService;

    public GamePersistenceBatchQueueService(GameStateService gameStateService) {
        this.gameStatesToPersistQueueFirst = new ConcurrentLinkedDeque<>();
        this.gameStatesToPersistQueueSecond = new ConcurrentLinkedDeque<>();
        this.gameStateService = gameStateService;
    }

    // TODO: Add description why use 2 queues
    // TODO: Add description what would change here in real implementation
    @Retryable(maxAttempts = DEFAULT_RETRY_COUNT_PERSISTING_THE_RESULT)
    @Scheduled(fixedRate = DEFAULT_INTERVAL_TO_PERSIST_THE_RESULT)
    void scheduleWithFixedRate() {
        isFirstQueueCurrent = !isFirstQueueCurrent;
        if (!isFirstQueueCurrent) {
            if (!gameStatesToPersistQueueFirst.isEmpty()) {
                gameStateService.persistMultipleGameStatesInBatches(gameStatesToPersistQueueFirst);
                counterPersistedGameStatesLastMinute.addAndGet(gameStatesToPersistQueueFirst.size());
                gameStatesToPersistQueueFirst.clear();
            }
        } else {
            if (!gameStatesToPersistQueueSecond.isEmpty()) {
                gameStateService.persistMultipleGameStatesInBatches(gameStatesToPersistQueueSecond);
                counterPersistedGameStatesLastMinute.addAndGet(gameStatesToPersistQueueSecond.size());
                gameStatesToPersistQueueSecond.clear();
            }
        }
    }

    @Scheduled(cron = "0 * * ? * *")
    void logNumberOfPersistedGameStatesLastMinute() {
        log.info(String.format("%d game states were persisted", counterPersistedGameStatesLastMinute.get()));
        counterPersistedGameStatesLastMinute.set(0);
    }

    public void addElementToPersistentQueue(GameStateModel gameState) {
        if (isFirstQueueCurrent) {
            gameStatesToPersistQueueFirst.add(gameState);
        } else {
            gameStatesToPersistQueueSecond.add(gameState);
        }
    }
}