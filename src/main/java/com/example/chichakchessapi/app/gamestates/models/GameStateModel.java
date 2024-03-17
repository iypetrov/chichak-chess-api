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
    private String id;
    private GameModel game;
    private String boardState;
    private PieceColor activeColor;
    private String castleAvailability;
    private String enPassantTargetSquare;
    private Integer halfmoveClock;
    private Integer fullmoveNumber;
    private Boolean isFinal;
    private Timestamp createdOn;

    public GameStateModel(
            String fen,
            String gameStateID,
            GameModel game,
            Boolean isFinal,
            Timestamp createdOn
    ) {
        String[] parts = fen.split("\\s+");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid FEN string: " + fen);
        }

        this.id = gameStateID;
        this.game = game;
        this.boardState = parts[0];
        this.activeColor = parts[1].equals("w") ? PieceColor.WHITE : PieceColor.BLACK;
        this.castleAvailability = parts[2];
        this.enPassantTargetSquare = parts[3];
        this.halfmoveClock = Integer.parseInt(parts[4]);
        this.fullmoveNumber = Integer.parseInt(parts[5]);
        this.isFinal = isFinal;
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        String characterActiveColor = (activeColor == PieceColor.WHITE) ? "w" : "b";
        return String.format("%s %s %s %s %s %s",
                boardState,
                characterActiveColor,
                castleAvailability,
                enPassantTargetSquare,
                halfmoveClock,
                fullmoveNumber);
    }
}