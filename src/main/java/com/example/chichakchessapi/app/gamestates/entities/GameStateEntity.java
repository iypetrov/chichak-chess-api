package com.example.chichakchessapi.app.gamestates.entities;

import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.games.entities.GameEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_state")
public class GameStateEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false, updatable = false)
    private GameEntity game;

    @Column(name = "board_state", nullable = false, updatable = false, length = 71)
    private String boardState;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_color", nullable = false, updatable = false, length = 5)
    private PieceColor activeColor;

    @Column(name = "castle_availability", nullable = false, updatable = false, length = 4)
    private String castleAvailability;

    @Column(name = "en_passant_target_square", nullable = false, updatable = false, length = 10)
    private String enPassantTargetSquare;

    @Column(name = "halfmove_clock", nullable = false, updatable = false)
    @Min(0)
    private Integer halfmoveClock;

    @Column(name = "fullmove_number", nullable = false, updatable = false)
    @Min(value = 0)
    private Integer fullmoveNumber;

    @Column(name = "is_final", nullable = false, updatable = false)
    private Boolean isFinal;

    @Column(name = "created_on", nullable = false, updatable = false)
    private Timestamp createdOn;
}