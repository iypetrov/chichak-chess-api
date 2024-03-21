package com.example.chichakchessapi.app.gameparticipants.entities;

import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.games.entities.GameEntity;
import com.example.chichakchessapi.app.players.entities.PlayerEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "game_participant",
        indexes = {
                @Index(name = "IDX_GAME_PARTICIPANT_PLAYER_ID", columnList = "player_id"),
                @Index(name = "IDX_GAME_PARTICIPANT_GAME_ID", columnList = "game_id")
        }
)
public class GameParticipantEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false, updatable = false)
    private GameEntity game;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "player_id", referencedColumnName = "id", nullable = false, updatable = false)
    private PlayerEntity player;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "opponent_id", referencedColumnName = "id", nullable = false, updatable = false)
    private PlayerEntity opponent;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false, updatable = false, length = 5)
    private PieceColor color;

    @Column(name = "is_winner")
    private Boolean isWinner;

    @Column(name = "is_draw")
    private Boolean isDraw;
}