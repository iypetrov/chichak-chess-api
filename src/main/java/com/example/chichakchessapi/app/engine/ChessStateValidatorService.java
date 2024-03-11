package com.example.chichakchessapi.app.engine;

import org.springframework.stereotype.Service;

@Service
public class ChessStateValidatorService {
    public static final String INIT_BOARD_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    private ChessStateValidatorService() {
        throw new IllegalStateException("Will be implemented in the next PR");
    }
}