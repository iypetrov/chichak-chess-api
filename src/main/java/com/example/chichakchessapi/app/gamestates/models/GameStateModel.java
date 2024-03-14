package com.example.chichakchessapi.app.gamestates.models;

import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.games.models.GameModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameStateModel {
    public GameStateModel(String fen) {
        this.id = fen;
    }

    private String id;
    private GameModel game;
    private String boardState;
    private PieceColor activeColor;
    private Boolean isCastleAvailable;
    private Integer fullmoveNumber;
    private Boolean isFinal;
    private Timestamp createdOn;

    // should be extended
    @Override
    public String toString() {
        return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    }
}