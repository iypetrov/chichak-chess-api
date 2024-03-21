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

    // The reason for using two caches instead of one is to avoid potential issues with flushing
    // If I used only one cache, there's a chance of encountering this issue:
    // When I start saving cached results, new events might come in that need to be saved for the next round
    // If I flush the cache directly in this situation, I could lose these new events received during the saving process
    // That's why, when I start saving the results, I divert newly received records into a separate cache while I save the results from the previous iteration

    // In a real implementation I would not use in memory message queue, but a distributed one (Kafka, RabbitMQ)
    // and will send the new state events to other service, which only job is to persist the result
    // It is possible this new persist service to store all these game states in NoSQL database (MongoDB, Cassandra) for better performance or in some more powerful relational database like TimescaleDB
    @Retryable(maxAttempts = DEFAULT_RETRY_COUNT_PERSISTING_THE_RESULT)
    @Scheduled(fixedRate = DEFAULT_INTERVAL_TO_PERSIST_THE_RESULT)
    void persistLatestGameStates() {
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
        log.info(String.format("%d game states were persisted in the last minute", counterPersistedGameStatesLastMinute.get()));
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